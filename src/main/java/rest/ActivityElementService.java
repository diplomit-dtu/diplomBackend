package rest;

import business.impl.GoogleActivityElementController;
import business.interfaces.ActivityElementController;
import data.dbDTO.CourseActivityElement;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Created by Christian on 31-05-2017.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("activityelements")
public class ActivityElementService {
    ActivityElementController activityElementController = new GoogleActivityElementController();

    @GET
    @Path("id/{id}") //For native Elements
    public CourseActivityElement getActivityElementById(@PathParam("id") String id){
        return null;
    }

    @GET
    @Path("googleid/{id}")
    public CourseActivityElement getActivityElementByGoogleID(@PathParam("id") String googleId) throws ValidException, PersistenceException {
        return activityElementController.getGoogleActivityElement(googleId);
    }


}
