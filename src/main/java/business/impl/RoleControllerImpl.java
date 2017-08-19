package business.impl;

import auth.Permission;
import auth.SecureEndpoint;
import business.interfaces.RoleController;
import data.dbDTO.Role;
import data.interfaces.PersistenceException;
import data.interfaces.RoleDAO;
import data.mongoImpl.MongoRoleDAO;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 08-06-2017.
 */
public class RoleControllerImpl implements RoleController {
    RoleDAO roleDAO = new MongoRoleDAO();
    @Override
    public Role getRole(String roleId) throws ValidException, PersistenceException {
        return roleDAO.get(roleId);
    }

    @Override
    public List<Role> getAllRoles() throws PersistenceException {
        return roleDAO.getAll();
    }

    @Override
    public Role getPortalAdmin() {
        Role role = new Role();
        role.setRoleName("PortalAdmin");
        role.getPermissions().add(Permission.PORTAL_ADMIN);
        return role;
    }

    @Override
    public Role getCourseAdmin() {
        Role role = new Role();
        role.setRoleName("CourseAdmin");
        role.getPermissions().add(Permission.CREATE_COURSE);
        return role;
    }


    @Override
    @SecureEndpoint(Permission.ADMIN_ROLES)
    public Role createRole(Role role) throws PersistenceException, DBValidationException {
        return roleDAO.save(role);
    }
}
