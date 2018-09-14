package business.interfaces;

import data.dbDTO.Link;
import data.dbDTO.LinkCollection;
import data.interfaces.PersistenceException;

import java.util.List;

/**
 * Created by Christian on 18-05-2017.
 */
public interface LinkController {
    List<Link> getDefaultLinks() throws PersistenceException, ValidException;

    Link saveDefaultLink(Link link) throws ValidException, PersistenceException;

    String deleteDefaultLink(String id) throws ValidException, PersistenceException;

    LinkCollection getLinkCollectionById(String id) throws ValidException, PersistenceException;
}
