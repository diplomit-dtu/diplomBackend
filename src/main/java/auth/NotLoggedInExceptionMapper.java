package auth;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Christian on 26-06-2017.
 */
public class NotLoggedInExceptionMapper implements ExceptionMapper<NotLoggedInException> {
    @Override
    public Response toResponse(NotLoggedInException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity("User Not logged in").build();
    }
}
