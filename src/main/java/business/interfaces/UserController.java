package business.interfaces;

import data.dbDTO.Role;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import rest.ElementNotFoundException;

import java.util.List;

/**
 * Created by Christian on 08-06-2017.
 */
public interface UserController {
    User getUserByCampusNetId(String cnId) throws ElementNotFoundException, PersistenceException;
    User saveUser(User user) throws PersistenceException;

    User getAnonymous();

    List<User> getAllUsers() throws PersistenceException;

    List<Role> getUserRoles(String userId) throws ElementNotFoundException, PersistenceException;

    List<Role> updateGlobalUserRoles(List<Role> roles, String userName) throws PersistenceException, ElementNotFoundException;
}
