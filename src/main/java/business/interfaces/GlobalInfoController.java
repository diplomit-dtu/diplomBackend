package business.interfaces;

import data.dbDTO.Link;
import data.interfaces.PersistenceException;
import rest.ValidException;

/**
 * Created by Christian on 18-05-2017.
 */
public interface GlobalInfoController {
    Link saveDefaultLink(Link link) throws ValidException, PersistenceException;
}
