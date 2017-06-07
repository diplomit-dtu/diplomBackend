package data.mongoImpl;

import data.dbDTO.Role;
import data.interfaces.BaseDAO;

/**
 * Created by Christian on 07-06-2017.
 */
public class MongoRoleDAO extends MongoBaseDAO<Role> implements BaseDAO<Role> {
    public MongoRoleDAO() {
        super(Role.class);
    }
}
