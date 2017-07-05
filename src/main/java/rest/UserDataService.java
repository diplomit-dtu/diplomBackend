package rest;

import business.interfaces.UserDataController;
import data.ControllerRegistry;
import data.dbDTO.UserData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by Christian on 05-07-2017.
 */
@Path("userdata")
public class UserDataService {
    UserDataController userDataController = ControllerRegistry.getUserDataController();

    @GET
    @Path("{id}")
    public UserData getUserData(@PathParam("id") String id){
        return userDataController.getUserData(id);

    }

}
