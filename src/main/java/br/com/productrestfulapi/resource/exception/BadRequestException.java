package br.com.productrestfulapi.resource.exception;


import com.sun.jersey.api.Responses;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class BadRequestException extends WebApplicationException {

    private static final long serialVersionUID = 1L;


    public BadRequestException(JSONObject messageReturn) {
        super(Response.status(Responses.CLIENT_ERROR).entity(messageReturn).type("application/json;charset=UTF-8").build());
    }
}
