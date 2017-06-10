package br.com.productrestfulapi.acceptancetests;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import io.restassured.response.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 06/06/17.
 */


public class ProductResourceTest {

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
    }

    @Test
    public void should_update_product() throws Exception {
        deleteProductsDataBase();
        instanciateProductOneWithImages();
        final long id = DataBaseUtils.persistProductsDataBaseAndGetId(productOne);
        JPAUtil.shutdown();
        logger.log(Level.INFO, url);
        final String productName = "Namde Update";
        JSONObject productToUpdate = getJsonProduct(productName,id);
        Response response = given().contentType("application/json").and().body(productToUpdate.toString()).put(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Updated",response.jsonPath().get("messageReturn"));
        DataBaseUtils.assertProductWasUpdated(productName);
    }

    @Test
    public void should_create_product() throws Exception {
        deleteProductsDataBase();
        logger.log(Level.INFO, url);
        final String productName = "MyProduct";
        JSONObject productToCreate = getJsonProduct(productName);
        Response response = given().contentType("application/json").and().body(productToCreate.toString()).post(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Created",response.jsonPath().get("messageReturn"));
        assertProductWasCreated();
    }

    @Test
    public void should_delete_product() throws Exception {
        persistProductsDataBase();
        final long productId = productOne.getId();
        String urlDelete = url + "/" + productId;
        logger.log(Level.INFO, urlDelete);
        Response response = given().contentType("application/json").and().delete(urlDelete);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productId+" was Deleted",response.jsonPath().get("messageReturn"));
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    @Test
    @Ignore
    public void get_all_products_including_specified_relationships() throws Exception {
        // java.lang.IllegalArgumentException: Path get(0).name is invalid.
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

    private JSONObject getJsonProduct(String productName, long id) throws JSONException {
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("id", id);
        productToCreate.put("name", productName);
        productToCreate.put("description","This is my product");
        // TODO productToCreate.put("parentProductID","");
        return productToCreate;
    }

    private JSONObject getJsonProduct(String productName) throws JSONException {
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("name", productName);
        productToCreate.put("description","This is my product");
        // TODO productToCreate.put("parentProductID","");
        return productToCreate;
    }

    private void deleteProductsDataBase() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.flush();
        em.getTransaction().commit();
        em.close();
        JPAUtil.shutdown();
    }

    private void assertProductWasCreated() throws IOException {
        em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Product c");
        List<Product> results = query.getResultList();
        assertNotNull(results);
        assertEquals(1,results.size());
        // TODO assert productDataBase
    }

    private void persistProductsDataBase() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        instanciateProductOneWithImages();
        em.persist(productOne);
        em.flush();
        em.getTransaction().commit();
        em.close();
        JPAUtil.shutdown();
    }

    private void instanciateProductOneWithImages() {
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProductOne = new ArrayList<Image>();
        imagesProductOne.add(new Image(productOne));
        imagesProductOne.add(new Image(productOne));
        productOne.setImages(imagesProductOne);
        // TODO Product Parent ?
    }
}
