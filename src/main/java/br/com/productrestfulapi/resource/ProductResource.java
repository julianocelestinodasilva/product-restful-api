package br.com.productrestfulapi.resource;

import br.com.productrestfulapi.model.ProductRepository;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by juliano on 06/06/17.
 */

@Path("/product")
public class ProductResource {

    private ProductRepository repository;
    private JSONObject messageReturn;

    public ProductResource() {
        try {
            EntityManager em = createEntityManager();
            repository = new ProductRepository(em);
            messageReturn = new JSONObject();
        } catch (IOException e) {
            // TODO WebApp Exception status code 500
            //throw new RuntimeException("An error occurred while trying to create ProductResource. " + e);
            e.printStackTrace();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject delete(@PathParam("id") long id) throws JSONException {
        repository.delete(id);
        messageReturn.put("messageReturn","Product "+id+" was Deleted");
        return messageReturn;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject update(JSONObject product) throws JSONException {
        if (product.has("id") && product.has("name")  && product.has("description")) {
            String productName = product.getString("name");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn","Product "+productName+" was Updated");
            return jsonObject;
        }
        return null; // TODO status code
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject create(JSONObject product) throws JSONException {
        if (product.has("name")  && product.has("description")) {
            String productName = product.getString("name");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn","Product "+productName+" was Created");
            return jsonObject;
        }
        return null; // TODO status code
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONArray getAllIncludingRelationships() throws JSONException {
        JSONArray jsonObject = new JSONArray();
        JSONObject product0 = new JSONObject();
        product0.put("name", "Product0");
        product0.put("description","The Product0");
        product0.put("parentProductId",999);
        product0.put("image",1000);
        jsonObject.put(product0);
        JSONObject product1 = new JSONObject();
        product1.put("name", "Product1");
        product1.put("description","The Product1");
        product1.put("parentProductId",999);
        product1.put("image",1000);
        jsonObject.put(product1);
        return jsonObject;
    }

    private EntityManager createEntityManager() throws IOException {
        // TODO Extrair p/ uma classe
        Map cfg = new HashMap<String,String>();
        Properties arquivoConexao = new Properties();
        arquivoConexao.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conexao.properties"));
        cfg.put("javax.persistence.jdbc.driver", arquivoConexao.getProperty("bd.productrestfulapi.driver"));
        cfg.put("javax.persistence.jdbc.url", arquivoConexao.getProperty("bd.productrestfulapi.url"));
        cfg.put("javax.persistence.jdbc.user", arquivoConexao.getProperty("bd.productrestfulapi.user"));
        cfg.put("javax.persistence.jdbc.password", arquivoConexao.getProperty("bd.productrestfulapi.password"));
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("productrestfulapi", cfg);
        return factory.createEntityManager();
    }
}