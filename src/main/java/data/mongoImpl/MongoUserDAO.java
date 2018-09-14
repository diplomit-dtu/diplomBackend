package data.mongoImpl;

import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import data.interfaces.ElementNotFoundException;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class MongoUserDAO extends MongoBaseDAO<User> implements UserDAO {
    public MongoUserDAO() {
        super(User.class);
    }

    @Override
    public User findByCampusNetId(String campusNetId) throws PersistenceException, ElementNotFoundException {
        List<User> users = findByField(User.userNameString, campusNetId);
        if (users.size()<1){
            throw new ElementNotFoundException("No such user");
        } else if (users.size() >1){
            throw new PersistenceException("Multiple Users found for Unique ID");
        }

        return users.get(0);
    }
}
