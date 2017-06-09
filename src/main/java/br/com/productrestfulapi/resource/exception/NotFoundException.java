package br.com.productrestfulapi.resource.exception;


import com.sun.jersey.api.Responses;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {

    private static final long serialVersionUID = 1L;


    public NotFoundException(JSONObject messageReturn) {
        super(Response.status(Responses.NOT_FOUND).entity(messageReturn).type("application/json;charset=UTF-8").build());
    }
}
