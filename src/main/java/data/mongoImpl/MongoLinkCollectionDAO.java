package data.mongoImpl;

import data.dbDTO.Link;
import data.dbDTO.LinkCollection;
import data.dbDTO.User;
import data.interfaces.LinkCollectionDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class MongoLinkCollectionDAO extends MongoBaseDAO<LinkCollection> implements LinkCollectionDAO {
    public MongoLinkCollectionDAO() {
        super(LinkCollection.class);
    }

    @Override
    public LinkCollection save(LinkCollection linkCollection) throws PersistenceException {
        saveLinks(linkCollection.getLinks());
        saveUser(linkCollection.getOwner());
        return super.save(linkCollection);
    }

    private User saveUser(User user) throws PersistenceException {
        UserDAO userDAO = new MongoUserDAO();
        return userDAO.save(user);
    }

    private List<Link> saveLinks(List<Link> links) throws PersistenceException {
        LinkDAO linkDAO = new MongoLinkDAO();
        linkDAO.saveMultiple(links);
        return links;
    }

    @Override
    public LinkCollection getByCourse(String Course) {
        return null;
    }
}
