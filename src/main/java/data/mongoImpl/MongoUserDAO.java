package data.mongoImpl;

import data.dbDTO.User;
import data.interfaces.UserDAO;

/**
 * Created by Christian on 11-05-2017.
 */
public class MongoUserDAO extends MongoBaseDAO<User> implements UserDAO {
    public MongoUserDAO() {
        super(User.class);
    }
}
