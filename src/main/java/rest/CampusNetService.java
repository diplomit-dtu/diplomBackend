package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/** Service for Campusnet redirects
 * Created by Christian on 28-04-2017.
 */
@Path("cn")
public class CampusNetService {

    @GET
    public Response getRedirect(@QueryParam("ticket") String ticket){
        return Response.ok().entity(ticket).build();
    }
}
