package business.impl;

import business.interfaces.UserDataController;
import data.dbDTO.UserData;
import data.interfaces.PersistenceException;
import data.interfaces.UserDataDAO;
import data.mongoImpl.MongoUserDataDAO;

/**
 * Created by Christian on 05-07-2017.
 */
public class UserDataControllerImpl implements UserDataController {
    UserDataDAO userDataDAO = new MongoUserDataDAO();

    @Override
    public UserData getUserData(String id) {

        return null;
    }

    @Override
    public UserData create(UserData userData) throws PersistenceException {
            return userDataDAO.save(userData);
    }
}
