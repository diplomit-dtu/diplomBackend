package rest;

import config.Config;
import deployment.DeployConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;

/** Service for Campusnet redirects
 * Created by Christian on 28-04-2017.
 */
@Path("cn")
public class CampusNetService {
    @Path("login")
    @GET
    public Response getLogin(){
        URI uri = URI.create(Config.CAMPUSNET_LOGIN_URL + "?service=" + DeployConfig.CN_REDIRECT_URL);
        return Response.temporaryRedirect(uri).build();
    }

    @GET
    public Response getRedirect(@QueryParam("ticket") String ticket){

        return Response.ok().entity("ticket:" + ticket).build();
    }
}
