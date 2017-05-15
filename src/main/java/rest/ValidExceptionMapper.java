package rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Christian on 15-05-2017.
 */
@Provider
public class ValidExceptionMapper implements ExceptionMapper<ValidException> {
    @Override
    public Response toResponse(ValidException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Validation Failed - Exception was: " + exception.getMessage())
                .build();

    }
}
