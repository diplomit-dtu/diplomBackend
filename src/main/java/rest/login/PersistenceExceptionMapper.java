package rest.login;

import data.interfaces.PersistenceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Christian on 15-05-2017.
 */
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException>{

    @Override
    public Response toResponse(PersistenceException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error while Trying to persist changes : "+ exception.getMessage())
                .build();
    }
}
