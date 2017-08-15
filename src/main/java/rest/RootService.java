package rest;

import auth.UserUtil;
import data.dbDTO.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/** Root Servlet
 * Created by Christian on 28-04-2017.
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class RootService {
    @Context
    ContainerRequestContext context;

    @GET
    public String getRoot(){
        return "API Description coming up <br>see <a href=\"rest/application.wadl\">application.wadl</a>";
    }

    @Path("userTest")
    @GET
    public User getUserFromContext(){
        System.out.println(context);
        return UserUtil.getUserFromContext(context);
    }

    @Path("contextTest")
    @GET
    public Collection<String> getContext(){
        return context.getPropertyNames();
    }
}
