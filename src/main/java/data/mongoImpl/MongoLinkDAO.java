package data.mongoImpl;

import data.dbDTO.Link;
import data.interfaces.LinkDAO;

/**
 * Created by Christian on 05-05-2017.
 */
public class MongoLinkDAO extends MongoBaseDAO<Link> implements LinkDAO {
    public MongoLinkDAO() {
        super(Link.class);
    }
}
