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
import rest.ValidException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Christian on 18-05-2017.
 */
public class LinkControllerImpl implements LinkController {
    GlobalInfoDAO globalInfoDAO = new MongoGlobalInfoDAO();
    LinkCollectionDAO linkCollectionDAO = new MongoLinkCollectionDAO();
    LinkDAO linkDAO = new MongoLinkDAO();

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

    @Override
    public String deleteDefaultLink(String id) throws ValidException, PersistenceException {
        LinkCollection linkCollection = getDefaultLinkCollection();
        System.out.println(id);
        //TODO improve performance
        List<Link> links = linkCollection.getLinks();
        for (Iterator<Link> linkIterator = links.iterator();linkIterator.hasNext();){
            Link link = linkIterator.next();
            System.out.println(link.getId());
            if (link.getId().equals(id)) {
                linkIterator.remove();
                linkCollectionDAO.save(linkCollection);
                linkDAO.delete(id);
                return link.getId();
            }
        }

        throw new ValidException("Link not found - could not delete");
    }

    @Override
    public LinkCollection getLinkCollectionById(String id) throws ValidException, PersistenceException {
        return linkCollectionDAO.get(id);
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
