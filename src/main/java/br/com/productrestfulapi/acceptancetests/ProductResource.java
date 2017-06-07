package br.com.productrestfulapi.acceptancetests;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juliano on 06/06/17.
 */

@Path("/product")
public class ProductResource {

    // http://docs.oracle.com/javaee/6/tutorial/doc/gilik.html

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject delete(@PathParam("id") long id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageReturn","Product "+id+" was Deleted");
        return jsonObject;
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
        jsonObject.put(new JSONObject());
        jsonObject.put(new JSONObject());
        return jsonObject;
    }
}