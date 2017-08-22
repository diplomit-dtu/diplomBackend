package rest.login;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** StubService for later implementation
 * Created by Christian on 29-04-2017.
 */
@Deprecated
@Path("login")
public class LoginService {

    @GET
    public Response test(){
        return Response.ok().entity("test").build();
    }

}
