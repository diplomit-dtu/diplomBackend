package data.mongoImpl;

import data.dbDTO.Role;
import data.interfaces.BaseDAO;
import data.interfaces.RoleDAO;

/**
 * Created by Christian on 07-06-2017.
 */
public class MongoRoleDAO extends MongoBaseDAO<Role> implements RoleDAO {
    public MongoRoleDAO() {
        super(Role.class);
    }
}
