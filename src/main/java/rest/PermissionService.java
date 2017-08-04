package rest;

import auth.Permission;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

/**
 * Created by Christian on 02-08-2017.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("permissions")
public class PermissionService {

    @GET
    public Permission[] getPermissions(){
        return Permission.values();
    }
}
