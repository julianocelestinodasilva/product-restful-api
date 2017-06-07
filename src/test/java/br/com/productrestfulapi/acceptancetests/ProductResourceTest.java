package br.com.productrestfulapi.acceptancetests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * Created by juliano on 06/06/17.
 */


public class ProductResourceTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String url;

    @Before
    public void setUp() throws Exception {
        url = "http://localhost:8080/productAPI/product";
        logger.log(Level.INFO, url);
    }

    @Test
    public void should_create_product() throws Exception {
        final String productName = "MyProduct";
        JSONObject productToCreate = new JSONObject();
        productToCreate.put("name", productName);
        productToCreate.put("description","This is my product");
        // TODO productToCreate.put("parentProductID","");
        Response response = given().contentType("application/json").and().body(productToCreate.toString()).post(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Product "+productName+" was Created",response.jsonPath().get("messageReturn"));
        // TODO Assert no Banco
    }

    @Test
    public void name() throws Exception {
        // TODO test name
        expect().statusCode(200).
                body("produto", equalTo("produto")).
                when().get(url);

    }
}
