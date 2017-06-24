package rest;

import business.impl.DBValidationException;
import business.impl.RoleControllerImpl;
import business.interfaces.RoleController;
import data.dbDTO.Role;
import data.interfaces.PersistenceException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by Christian on 24-06-2017.
 */
@Path("roles")
public class RoleService {
    private RoleController roleController = new RoleControllerImpl();

    @GET
    public List<Role> getAllRoles() throws PersistenceException {
        return roleController.getAllRoles();
    }

    @POST
    public Role createNewRole(Role role) throws DBValidationException, PersistenceException {
        return roleController.createRole(role);
    }

}
