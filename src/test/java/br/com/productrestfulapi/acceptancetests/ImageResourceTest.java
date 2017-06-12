package br.com.productrestfulapi.acceptancetests;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import io.restassured.response.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 06/06/17.
 */


public class ImageResourceTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String url;
    private EntityManager em;

    private Image image;
    private Product product;

    @Before
    public void setUp() throws Exception {
        url = URLApi.image();
    }

    @Test
    public void should_delete_image() throws Exception {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        image = DataBaseUtils.persistImageAndProduct(product);
        JPAUtil.shutdown();
        final long imageId = image.getId();
        String urlDelete = url + "/" + imageId;
        logger.log(Level.INFO, urlDelete);
        Response response = given().contentType("application/json").and().delete(urlDelete);
        assertEquals(200,response.getStatusCode());
        assertEquals("Image "+imageId+" was Deleted",response.jsonPath().get("messageReturn"));
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Image.class, imageId));
    }

    @Test
    public void should_create_image() throws Exception {
        DataBaseUtils.deleteEntities();
        final Product product = new Product("Primeiro Produto", "Primeiro Produto");
        final long id = DataBaseUtils.deleteEntitiesAndPersistProductAndGetId(product);
        product.setId(id);
        JPAUtil.shutdown();
        image = new Image(product);
        logger.log(Level.INFO, url);
        JSONObject json = getJson(product);
        Response response = given().contentType("application/json").and().body(json.toString()).post(url);
        assertEquals(200,response.getStatusCode());
        assertEquals("Image was Created",response.jsonPath().get("messageReturn"));
        DataBaseUtils.assertWasCreated(image);
    }

    private JSONObject getJson(Product product) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("idProduct", product.getId());
        return json;
    }
}
