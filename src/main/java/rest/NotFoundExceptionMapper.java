package rest;

import data.interfaces.ElementNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Christian on 16-05-2017.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<ElementNotFoundException> {
    @Override
    public Response toResponse(ElementNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).entity("Element not found!").build();
    }
}
