package business.impl;

import business.interfaces.LinkController;
import data.dbDTO.GlobalInfo;
import data.dbDTO.Link;
import data.dbDTO.LinkCollection;
import data.interfaces.GlobalInfoDAO;
import data.interfaces.LinkCollectionDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoGlobalInfoDAO;
import data.mongoImpl.MongoLinkCollectionDAO;
import data.mongoImpl.MongoLinkDAO;
import org.bson.types.ObjectId;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 18-05-2017.
 */
public class LinkControllerImpl implements LinkController {
    GlobalInfoDAO globalInfoDAO = new MongoGlobalInfoDAO();
    LinkCollectionDAO linkCollectionDAO = new MongoLinkCollectionDAO();

    @Override
    public List<Link> getDefaultLinks() throws PersistenceException, ValidException {
        //Get list of defaultLinks in GlobalInfo
        LinkCollection linkCollection = getDefaultLinkCollection();
        return linkCollection.getLinks();
    }

    @Override
    public Link saveDefaultLink(Link link) throws ValidException, PersistenceException {
        System.out.println(System.currentTimeMillis());
        LinkCollection linkCollection = getDefaultLinkCollection();
        linkCollection.getLinks().add(link);
        linkCollectionDAO.save(linkCollection);
        System.out.println(System.currentTimeMillis());
        return link;
    }

    private LinkCollection getDefaultLinkCollection() throws ValidException, PersistenceException {
        GlobalInfo info = globalInfoDAO.getInfo();
        LinkCollection linkCollection = linkCollectionDAO.get(info.getDefaultLinkCollectionId());
        if (linkCollection==null) {linkCollection = new LinkCollection();
            linkCollection.setId(info.getDefaultLinkCollectionId());
        }
        return linkCollection;
    }
}
