package business.interfaces;

import data.dbDTO.UserData;
import data.interfaces.PersistenceException;

/**
 * Created by Christian on 05-07-2017.
 */
public interface UserDataController {
    UserData getUserData(String id);

    UserData create(UserData userData) throws PersistenceException;
}
