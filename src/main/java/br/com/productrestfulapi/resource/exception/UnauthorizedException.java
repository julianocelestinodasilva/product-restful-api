package br.com.productrestfulapi.resource.exception;


import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UnauthorizedException extends WebApplicationException {

    private static final long serialVersionUID = 1L;
    private static final int UNAUTHORIZED = 401;

    public UnauthorizedException(JSONObject messageReturn) {
        super(Response.status(UNAUTHORIZED).entity(messageReturn).type("application/json;charset=UTF-8").build());
    }
}
