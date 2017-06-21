package rest;

import auth.Permission;
import auth.SecureEndpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** Testing Service
 * Created by Christian on 28-04-2017.
 */
@Path("hello")
public class HelloService {
    @GET
    public String getHello(){
        return "HELLO From Jersey!";
    }

    @GET
    @Path("secure")
    @SecureEndpoint({Permission.ADMIN_USERS,Permission.USER_UPDATE_SELF})
    public String getSecureHello(){
        return "Hello from secure!";
    }

}
