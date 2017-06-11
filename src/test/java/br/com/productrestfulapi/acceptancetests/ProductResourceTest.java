package br.com.productrestfulapi.acceptancetests;

import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import io.restassured.response.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
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

    // private List<Image> imagesProductOne;
    // private List<Image> imagesproductTwo;
    // private List<Image> imagesproductTree;

    @Before
    public void setUp() throws Exception {
        url = "http://localhost:8080/productAPI/product";
    }

    @Test
    public void get_all_products_including_specified_relationships() throws Exception {
        List<Product> productsWithRelationships = DataBaseUtils.persistProductsWithRelationships();
        JPAUtil.shutdown();
        Product productWithImages = productsWithRelationships.get(0);
        Product productWithParent = productsWithRelationships.get(1);
        logger.log(Level.INFO, url);
        expect().statusCode(200).
                body("size()", is(2)).
                body("get(0).name", equalTo(productWithImages.getName())).
                body("get(0).description", equalTo(productWithImages.getDescription())).
                body("get(0).image", equalTo(productWithImages.getImages().get(0).getId())).
                body("get(1).name", equalTo(productWithParent.getName())).
                body("get(1).description", equalTo(productWithParent.getDescription())).
                body("get(1).parentProductId", equalTo(productWithParent.getParent().getId())).
                when().get(url);
    }

    @Test
    public void should_update_product() throws Exception {
        DataBaseUtils.deleteProducts();
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        final long id = DataBaseUtils.persistProductsAndGetId(productOne);
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
        DataBaseUtils.deleteProducts();
        JPAUtil.shutdown();
        logger.log(Level.INFO, url);
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        final String productName = productOne.getName();
        JSONObject productToCreate = getJsonProduct(productOne);
        Response response = given().contentType("application/json").and().body(productToCreate.toString()).post(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Created",response.jsonPath().get("messageReturn"));
        DataBaseUtils.assertProductWasCreated(productOne);
    }

    @Test
    public void should_delete_product() throws Exception {
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        DataBaseUtils.persistProducts(productOne);
        JPAUtil.shutdown();
        final long productId = productOne.getId();
        String urlDelete = url + "/" + productId;
        logger.log(Level.INFO, urlDelete);
        Response response = given().contentType("application/json").and().delete(urlDelete);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productId+" was Deleted",response.jsonPath().get("messageReturn"));
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    private JSONObject getJsonProduct(String productName, long id) throws JSONException {
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("id", id);
        productToCreate.put("name", productName);
        productToCreate.put("description","This is my product");
        // TODO productToCreate.put("parentProductID","");
        return productToCreate;
    }

    private JSONObject getJsonProduct(Product product) throws JSONException {
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("name", product.getName());
        productToCreate.put("description",product.getDescription());
        // TODO productToCreate.put("parentProductID","");
        return productToCreate;
    }
}
