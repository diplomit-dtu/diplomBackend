package rest;

import auth.Permission;
import auth.SecureEndpoint;
import business.impl.UserControllerImpl;
import business.interfaces.UserController;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Christian on 07-06-2017.
 */
@Path("users")
public class UserService {
    UserController userController = new UserControllerImpl();
    @Context
    ContainerRequestContext requestContext;

    @GET
    @SecureEndpoint(Permission.PORTAL_ADMIN)
    public List<User> getAllUsers() throws PersistenceException {
        return userController.getAllUsers();
    }

    @Path("{id}/roles")
    public RoleResource getRoleResource(@PathParam("id") String id){
        return new RoleResource(id);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @SecureEndpoint(value = {Permission.ADMIN_ALLUSERS} )
    public class RoleResource {
        private final String userId;

        private RoleResource(String id) {
            this.userId = id;
        }

        @GET
        public List<Role> getUserRoles() throws ElementNotFoundException, PersistenceException {
            return userController.getUserRoles(userId);
        }

        @PUT
        public List<Role> updateUserRoles(List<Role> roles) throws ElementNotFoundException, PersistenceException {
            return userController.updateGlobalUserRoles(roles, userId);
        }


    }
}
