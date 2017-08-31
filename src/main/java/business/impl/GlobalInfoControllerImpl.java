package business.impl;

import business.interfaces.GlobalInfoController;
import data.dbDTO.Link;
import data.interfaces.GlobalInfoDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoGlobalInfoDAO;
import data.mongoImpl.MongoLinkDAO;
import business.interfaces.ValidException;

/**
 * Created by Christian on 18-05-2017.
 */
public class GlobalInfoControllerImpl implements GlobalInfoController {
    GlobalInfoDAO globalInfoDAO = new MongoGlobalInfoDAO();
    LinkDAO linkDAO = new MongoLinkDAO();
    @Override
    public Link saveDefaultLink(Link link) throws ValidException, PersistenceException {
        globalInfoDAO.getInfo();

        return null;
    }
}
