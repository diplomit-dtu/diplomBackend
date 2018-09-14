package rest;

import auth.Permission;
import business.interfaces.DBValidationException;
import business.impl.RoleControllerImpl;
import business.interfaces.RoleController;
import config.Config;
import data.dbDTO.Role;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Christian on 24-06-2017.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("roles")
public class RoleService {
    private RoleController roleController = new RoleControllerImpl();

    @Path("template")
    @GET
    public Role getTemplate(){
        Role role = new Role();
        role.getPermissions().add(Permission.PORTAL_ADMIN);
        role.setRoleName(Config.PORTAL_ADMIN);
        return role;
    }

    @GET
    public List<Role> getAllRoles() throws PersistenceException {
        return roleController.getAllRoles();
    }

    @POST
    public Role createNewRole(Role role) throws DBValidationException, PersistenceException {
        return roleController.createRole(role);
    }

}
