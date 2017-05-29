package rest;

import business.impl.GlobalInfoControllerImpl;
import business.interfaces.GlobalInfoController;
import data.dbDTO.Link;
import data.interfaces.PersistenceException;

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
