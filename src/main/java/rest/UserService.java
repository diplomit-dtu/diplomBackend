package rest;

import auth.Permission;
import auth.SecureEndpoint;
import auth.UserUtil;
import business.impl.AgendaControllerImpl;
import business.impl.UserControllerImpl;
import business.interfaces.AgendaController;
import business.interfaces.UserController;
import config.Config;
import data.dbDTO.AgendaInfo;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("users")
public class UserService {
    private UserController userController = new UserControllerImpl();
    @Context
    ContainerRequestContext requestContext;

    @GET
    @SecureEndpoint(Permission.PORTAL_ADMIN)
    public List<User> getAllUsers() throws PersistenceException {
        return userController.getAllUsers();
    }

    @GET
    @Path("{id}")
    public User getUserById(@PathParam("id") String id) throws ValidException, PersistenceException {
        return userController.get(id);
    }

    @GET
    @Path("self")
    public User getSelf() throws ValidException, PersistenceException {
        User userFromContext = UserUtil.getUserFromContext(requestContext);
        return userController.get(userFromContext.getId());
    }

    @Path("agendas")
    public UserAgendaResource getUserAgendaResource(){
        return new UserAgendaResource();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public class UserAgendaResource {
        AgendaController agendaController = new AgendaControllerImpl();
        @Context
        ContainerRequestContext requestContext;

        @Path("own")
        @POST
        public AgendaInfo addNewAgenda(AgendaInfo agendaInfo) throws ValidException, PersistenceException {
            User user = UserUtil.getUserFromContext(requestContext);
            AgendaInfo newAgenda = agendaController.createNewAgenda(agendaInfo, user.getId());
            return newAgenda;
        }
    }
}
