package rest;

import business.impl.GlobalInfoControllerImpl;
import business.impl.LinkControllerImpl;
import business.interfaces.GlobalInfoController;
import business.interfaces.LinkController;
import data.dbDTO.GlobalInfo;
import data.dbDTO.Link;
import data.interfaces.GlobalInfoDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoGlobalInfoDAO;
import data.mongoImpl.MongoLinkDAO;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Christian on 18-05-2017.
 */
@Path("globalinfo")
public class GlobalInfoService {
    GlobalInfoController globalInfoController = new GlobalInfoControllerImpl();

    @POST
    @Path("defaultlinks")
    public Link postDefaultLink(Link link) throws PersistenceException, ValidException {
        return globalInfoController.saveDefaultLink(link);
    }
}
