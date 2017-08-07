package rest;

import business.interfaces.UserDataController;
import business.ControllerRegistry;
import data.dbDTO.AgendaInfo;
import data.dbDTO.UserData;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Christian on 05-07-2017.
 */
@Path("userdata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserDataService {
    UserDataController userDataController = ControllerRegistry.getUserDataController();

    //For debug purposes
    @GET
    @Path("template")
    public UserData getTemplate(){
        UserData userData = new UserData();
        AgendaInfo agendaInfo = new AgendaInfo();
        agendaInfo.setCourseName("VidereGående Programmering");
        agendaInfo.setAgendaId("agendaID");
        return userData;
    }

    @GET
    @Path("{id}")
    public UserData getUserData(@PathParam("id") String id){
        return userDataController.getUserData(id);

    }

    @POST
    public UserData createUserData(UserData userData) throws PersistenceException {
        return userDataController.create(userData);
    }

}
