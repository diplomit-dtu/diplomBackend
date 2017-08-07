package rest;

import business.impl.CourseControllerImpl;
import business.interfaces.CourseController;
import data.dbDTO.CoursePlan;
import data.interfaces.CoursePlanDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoCoursePlanDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Christian on 15-05-2017.
 */
@Path("courseplans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoursePlanService {
    CourseController ctrl = new CourseControllerImpl();
    CoursePlanDAO coursePlanDAO = new MongoCoursePlanDAO();

    @GET //Template
    @Path("template")
    public CoursePlan getTemplate(){
        return ctrl.getCoursePlanTemplate();
    }

    @GET
    @Path("{id}")
    public CoursePlan getCoursePlan(@PathParam("id")String id) throws PersistenceException, ElementNotFoundException, ValidException {
        CoursePlan coursePlan = coursePlanDAO.get(id);
        System.out.println(coursePlan);
        return coursePlan;
    }

    @GET
    @Path("google/id/{id}")
    public CoursePlan getGoogleCoursePlan(@PathParam("id") String id) throws ValidException, PersistenceException {
        return ctrl.getGoogleCoursePlan(id);
    }

    @POST
    public CoursePlan createCourseplan(CoursePlan coursePlan) throws ValidException, PersistenceException {
        if(coursePlan.getObjectId()!=null) throw new ValidException("ObjectId may not be set when creating Courseplan");
        return ctrl.createCoursePlan(coursePlan);
    }
    @PUT
    public CoursePlan updateCourseplan(CoursePlan coursePlan) throws ValidException,PersistenceException {
        return ctrl.updateCoursePlan(coursePlan);
    }

}
