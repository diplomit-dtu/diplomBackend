package rest.login;

import auth.NotLoggedInException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotLoggedInMapper implements ExceptionMapper<NotLoggedInException> {
    @Override
    public Response toResponse(NotLoggedInException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
    }
}
