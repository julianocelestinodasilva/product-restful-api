package br.com.productrestfulapi.acceptancetests;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.Product;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 06/06/17.
 */


public class ProductResourceTest {

    private static final String ARQUIVO_CONEXAO_BD = "conexao.properties";
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String url;
    private EntityManager em;

    private Product productOne;
    private Product productTwo;
    private Product productWithParent;

    private List<Image> imagesProductOne;
    private List<Image> imagesproductTwo;
    private List<Image> imagesproductTree;

    @Before
    public void setUp() throws Exception {
        url = "http://localhost:8080/productAPI/product";
        createEntityManager();
    }

    @Test
    public void should_delete_product() throws Exception {
        persistProducts();
        final long productId = productOne.getId();
        String urlDelete = url + "/" + productId;
        logger.log(Level.INFO, urlDelete);
        Response response = given().contentType("application/json").and().delete(urlDelete);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productId+" was Deleted",response.jsonPath().get("messageReturn"));
        assertNull(em.find(Product.class, productId));
    }

    @Test
    public void get_all_products_including_specified_relationships() throws Exception {
        logger.log(Level.INFO, url);
        expect().statusCode(200).
                body("size()", is(2)).
                body("get(0).name", equalTo("Product0")).
                body("get(0).description", equalTo("The Product0")).
                body("get(0).parentProductId", equalTo(999)).
                body("get(0).image", equalTo(1000)).
                body("get(1).name", equalTo("Product1")).
                body("get(1).description", equalTo("The Product1")).
                body("get(1).parentProductId", equalTo(999)).
                body("get(1).image", equalTo(1000)).
                when().get(url);
        // TODO Assert no Banco
    }

    @Test
    public void should_update_product() throws Exception {
        logger.log(Level.INFO, url);
        final String productName = "MyProduct";
        JSONObject productToUpdate = getJsonProduct(productName);
        productToUpdate.put("id",9999L);
        Response response = given().contentType("application/json").and().body(productToUpdate.toString()).put(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Updated",response.jsonPath().get("messageReturn"));
        // TODO Assert no Banco
    }

    @Test
    public void should_create_product() throws Exception {
        logger.log(Level.INFO, url);
        final String productName = "MyProduct";
        JSONObject productToCreate = getJsonProduct(productName);
        Response response = given().contentType("application/json").and().body(productToCreate.toString()).post(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Created",response.jsonPath().get("messageReturn"));
        // TODO Assert no Banco
    }

    private JSONObject getJsonProduct(String productName) throws JSONException {
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("name", productName);
        productToCreate.put("description","This is my product");
        // TODO productToCreate.put("parentProductID","");
        return productToCreate;
    }

    private void persistProducts() {
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        persistProductOne();
        em.getTransaction().commit();
    }

    private void persistProductOne() {
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProductOne = new ArrayList<Image>();
        imagesProductOne.add(new Image(productOne));
        imagesProductOne.add(new Image(productOne));
        productOne.setImages(imagesProductOne);
        em.persist(productOne);
    }

    private void createEntityManager() throws IOException {
        Map cfg = new HashMap<String,String>();
        Properties arquivoConexao = new Properties();
        arquivoConexao.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(ARQUIVO_CONEXAO_BD));
        cfg.put("javax.persistence.jdbc.driver", arquivoConexao.getProperty("bd.productrestfulapi.driver"));
        cfg.put("javax.persistence.jdbc.url", arquivoConexao.getProperty("bd.productrestfulapi.url"));
        cfg.put("javax.persistence.jdbc.user", arquivoConexao.getProperty("bd.productrestfulapi.user"));
        cfg.put("javax.persistence.jdbc.password", arquivoConexao.getProperty("bd.productrestfulapi.password"));
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("productrestfulapi", cfg);
        em = factory.createEntityManager();
    }
}
