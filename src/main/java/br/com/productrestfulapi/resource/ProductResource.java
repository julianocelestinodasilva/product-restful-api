package br.com.productrestfulapi.resource;

import br.com.productrestfulapi.model.ProductRepository;
import br.com.productrestfulapi.util.JPAUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by juliano on 06/06/17.
 */

@Path("/product")
public class ProductResource {

    private ProductRepository repository;
    private EntityManager em;
    private JSONObject messageReturn;

    public ProductResource() {
        try {
            em = JPAUtil.createEntityManager();
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
    public JSONObject delete(@PathParam("id") long id) throws JSONException, IOException {
        repository.delete(id);
        em.close(); // TODO em.close()
        JPAUtil.shutdown();
        messageReturn.put("messageReturn","Product "+id+" was Deleted");
        return messageReturn;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject update(JSONObject product) throws JSONException, IOException {
        if (product.has("id") && product.has("name")  && product.has("description")) {
            String productName = product.getString("name");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn","Product "+productName+" was Updated");
            JPAUtil.shutdown();
            return jsonObject;
        }
        return null; // TODO status code
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject create(JSONObject product) throws JSONException, IOException {
        if (product.has("name")  && product.has("description")) {
            String productName = product.getString("name");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn","Product "+productName+" was Created");
            JPAUtil.shutdown();
            return jsonObject;
        }
        return null; // TODO status code
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONArray getAllIncludingRelationships() throws JSONException, IOException {
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
        JPAUtil.shutdown();
        return jsonObject;
    }
}