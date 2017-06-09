package business.impl;

import business.interfaces.UserController;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import data.mongoImpl.MongoUserDAO;
import rest.ElementNotFoundException;

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
}
