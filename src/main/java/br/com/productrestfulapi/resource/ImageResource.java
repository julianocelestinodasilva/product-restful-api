package br.com.productrestfulapi.resource;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.ImageRepository;
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

@Path("/image")
public class ImageResource {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ImageRepository repository;
    private EntityManager em;
    private JSONObject messageReturn;

    public ImageResource() throws JSONException {
        try {
            em = JPAUtil.createEntityManager();
            repository = new ImageRepository(em);
            messageReturn = new JSONObject();
        } catch (IOException e) {
            final String erroMessage = "An error occurred while trying to create ImageResource. " + e;
            logger.log (Level.SEVERE, erroMessage);
            messageReturn.put("messageReturn", erroMessage);
            throw new InternalServerErrorException(messageReturn);
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{id}")
    public JSONObject delete(@PathParam("id") long id) throws JSONException {
        try {
            boolean wasDeleted = repository.delete(id);
            if (!wasDeleted) {
                messageReturn.put("messageReturn", "Image "+id+" not Found");
                throw new NotFoundException(messageReturn);
            }
            messageReturn.put("messageReturn","Image "+id+" was Deleted");
            return messageReturn;
        } finally {
            em.close();
            shutdown();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject create(JSONObject imageJson) throws JSONException {
        try {
            Image image = Image.getFromJSON(imageJson);
            repository.create(image);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageReturn", "Image was Created");
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