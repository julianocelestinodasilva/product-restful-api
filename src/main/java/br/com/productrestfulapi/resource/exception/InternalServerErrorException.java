package br.com.productrestfulapi.resource.exception;


import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InternalServerErrorException extends WebApplicationException {

    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(JSONObject messageReturn) {
        super(Response.status(INTERNAL_SERVER_ERROR).entity(messageReturn).type("application/json;charset=UTF-8").build());
    }
}
