package business.impl;

import business.interfaces.UserController;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import data.mongoImpl.MongoUserDAO;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 08-06-2017.
 */
public class UserControllerImpl implements UserController {
    UserDAO userDAO = new MongoUserDAO();
    @Override
    public User getUserByCampusNetId(String cnId) throws ElementNotFoundException, PersistenceException {
        return userDAO.findByCampusNetId(cnId);
    }

    @Override
    public User saveUser(User user) throws PersistenceException {
        return userDAO.save(user);
    }

    @Override
    public User getAnonymous() {
        User user = new User();
        user.setRoles(null);
        return user;
    }

    @Override
    public List<User> getAllUsers() throws PersistenceException {
        return userDAO.getAll();
    }

    @Override
    public List<Role> getUserRoles(String userId) throws ElementNotFoundException, PersistenceException {
        User user = userDAO.findByCampusNetId(userId);
        return user.getRoles();
    }

    @Override
    public List<Role> updateGlobalUserRoles(List<Role> roles, String userName) throws PersistenceException, ElementNotFoundException {
        int noUpdated = userDAO.findByFieldAndUpdateField("userName", userName, "roles", roles);
        if (noUpdated<=0) {throw new ElementNotFoundException("no user with that userName found");}
        else if (noUpdated==1) return roles;
        else {
            throw new PersistenceException("Multiple users with same ID found");
        }
    }

    @Override
    public User get(String userId) throws ValidException, PersistenceException {
        return userDAO.get(userId);
    }
}
