package data.mongoImpl;

import data.MorphiaHandler;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import org.mongodb.morphia.Datastore;
import rest.ElementNotFoundException;

import javax.ws.rs.NotFoundException;
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
