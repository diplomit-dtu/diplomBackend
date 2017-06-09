package business.interfaces;

import data.dbDTO.User;
import data.interfaces.PersistenceException;
import rest.ElementNotFoundException;

/**
 * Created by Christian on 08-06-2017.
 */
public interface UserController {
    User getUserByCampusNetId(String cnId) throws ElementNotFoundException, PersistenceException;
    User saveUser(User user) throws PersistenceException;

    User getAnonymous();
}
