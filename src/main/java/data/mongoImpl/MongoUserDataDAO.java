package data.mongoImpl;

import data.dbDTO.UserData;
import data.interfaces.UserDataDAO;

/**
 * Created by Christian on 05-07-2017.
 */
public class MongoUserDataDAO extends MongoBaseDAO<UserData> implements UserDataDAO{
    public MongoUserDataDAO() {
        super(UserData.class);
    }
}
