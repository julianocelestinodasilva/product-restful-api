package br.com.productrestfulapi.acceptancetests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.CoreMatchers.equalTo;

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
    public void name() throws Exception {
        expect().statusCode(200).
                body("produto", equalTo("produto")).
                when().get(url);

    }
}
