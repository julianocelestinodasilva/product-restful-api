package br.com.productrestfulapi.resource;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.model.ProductRepository;
import br.com.productrestfulapi.resource.exception.BadRequestException;
import br.com.productrestfulapi.resource.exception.InternalServerErrorException;
import br.com.productrestfulapi.resource.exception.NotFoundException;
import br.com.productrestfulapi.util.JPAUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by juliano on 06/06/17.
 */

@Path("/product")
public class ProductResource {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ProductRepository repository;
    private EntityManager em;
    private JSONObject messageReturn;

    public ProductResource() throws JSONException {
        try {
            em = JPAUtil.createEntityManager();
            repository = new ProductRepository(em);
            messageReturn = new JSONObject();
        } catch (IOException e) {
            final String erroMessage = "An error occurred while trying to create ProductResource. " + e;
            logger.log (Level.SEVERE, erroMessage);
            messageReturn.put("messageReturn", erroMessage);
            throw new InternalServerErrorException(messageReturn);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject get(@PathParam("id") long id) throws JSONException {
        try {
            Product product = repository.get(id);
            if (product == null) {
                messageReturn.put("messageReturn", "Product not Found");
                throw new NotFoundException(messageReturn);
            }
            return createJSONObject(product,false);
        } finally {
            em.close();
            shutdown();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/and-relationships/{id}")
    public JSONObject getWithRelationships(@PathParam("id") long id) throws JSONException {
        try {
            Product product = repository.get(id);
            if (product == null) {
                messageReturn.put("messageReturn", "Product not Found");
                throw new NotFoundException(messageReturn);
            }
            return createJSONObject(product,true);
        } finally {
            em.close();
            shutdown();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONArray get() throws JSONException {
        try {
            List<Product> products = repository.get();
            if (products == null || products.size() < 1) {
                messageReturn.put("messageReturn", "Products not Found");
                throw new NotFoundException(messageReturn);
            }
            return createJSONArray(products,false);
        } finally {
            em.close();
            shutdown();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/and-relationships")
    public JSONArray getWithRelationships() throws JSONException {
        try {
            List<Product> products = repository.get();
            if (products == null || products.size() < 1) {
                messageReturn.put("messageReturn", "Products not Found");
                throw new NotFoundException(messageReturn);
            }
            return createJSONArray(products,true);
        } finally {
            em.close();
            shutdown();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject update(@PathParam("id") long id,JSONObject productJson) throws JSONException {
        try {
            Product product = Product.getFromJSON(id,productJson);
            repository.update(product);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn","Product "+product.getName()+" was Updated");
            return jsonObject;
        } catch (IllegalArgumentException e) {
            logger.log (Level.WARNING, e.getMessage());
            messageReturn.put("messageReturn", e.getMessage());
            throw new BadRequestException(messageReturn);
        } finally {
            em.close();
            shutdown();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject create(JSONObject productJson) throws JSONException {
        try {
            Product product = Product.getFromJSON(productJson);
            repository.create(product);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn", "Product " + product.getName()+ " was Created");
            return jsonObject;
        } catch (IllegalArgumentException e) {
            logger.log (Level.WARNING, e.getMessage());
            messageReturn.put("messageReturn", e.getMessage());
            throw new BadRequestException(messageReturn);
        } finally {
            em.close();
            shutdown();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject delete(@PathParam("id") long id) throws JSONException {
        try {
            boolean wasDeleted = repository.delete(id);
            if (!wasDeleted) {
                messageReturn.put("messageReturn", "Product "+id+" not Found");
                throw new NotFoundException(messageReturn);
            }
            messageReturn.put("messageReturn","Product "+id+" was Deleted");
            return messageReturn;
        } finally {
            em.close();
            shutdown();
        }
    }

    private JSONArray createJSONArray(List<Product> products, boolean withRelationships) throws JSONException {
        JSONArray jsonObject = new JSONArray();
        for (Product product : products) {
            JSONObject productJson = createJSONObject(product,withRelationships);
            jsonObject.put(productJson);
        }
        return jsonObject;
    }

    private JSONObject createJSONObject(Product product, boolean withRelationships) throws JSONException {
        JSONObject productJson = new JSONObject();
        productJson.put("id", product.getId());
        productJson.put("name", product.getName());
        productJson.put("description", product.getDescription());
        if (withRelationships) {
            Product parent = product.getParent();
            if (parent != null && parent.getId() > 0) {
                productJson.put("parentProductId", parent.getId());
            }
            putImagesOnJSONObject(product, productJson);
        }
        return productJson;
    }

    private void putImagesOnJSONObject(Product product, JSONObject productJson) throws JSONException {
        List<Long> imagesId = new ArrayList<Long>();
        final List<Image> images = product.getImages();
        if (images != null) {
            for (Image image : images) {
                imagesId.add(image.getId());
            }
            productJson.put("images", imagesId);
        }
    }

    private void shutdown() throws JSONException {
        try {
            JPAUtil.shutdown();
        } catch (IOException e) {
            final String erroMessage = "An error occurred while trying to shutdown database. " + e;
            logger.log (Level.SEVERE, erroMessage);
            messageReturn.put("messageReturn", erroMessage);
            throw new InternalServerErrorException(messageReturn);
        }
    }
}