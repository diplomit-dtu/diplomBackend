package auth;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Christian on 24-06-2017.
 */
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {
    @Override
    public Response toResponse(AccessDeniedException exception) {
        return null;
    }
}
