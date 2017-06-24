package rest;

import business.impl.DBValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Christian on 24-06-2017.
 */
public class DBValidationExceptionMapper implements ExceptionMapper<DBValidationException> {
    @Override
    public Response toResponse(DBValidationException exception) {
        return Response.status(Response.Status.CONFLICT).entity("Role name already exists").build();
    }
}
