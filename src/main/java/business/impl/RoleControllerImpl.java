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
    @SecureEndpoint(Permission.ADMIN_ROLES)
    public Role createRole(Role role) throws PersistenceException, DBValidationException {
        List<Role> rolesWithSameName = roleDAO.findByField(Role.ROLE_NAME, role.getRoleName());
        if (rolesWithSameName!=null || rolesWithSameName.size()>0){
            throw new DBValidationException("Role with same name already exists");
        }
        return roleDAO.save(role);
    }
}
