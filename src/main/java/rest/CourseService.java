package rest;

import business.impl.CourseControllerImpl;
import business.interfaces.CourseController;
import data.dbDTO.Course;
import data.interfaces.PersistenceException;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
public class CourseService {
    CourseController ctrl = new CourseControllerImpl();

    @GET //ALL
    public List<Course> getCourses(){
        return ctrl.getCourses();
    }

    @GET
    @Path("{id}")
    public Course getCourse(@PathParam("id") String id) throws PersistenceException, ValidException {
        return ctrl.getCourse(id);
    }

    @POST
    public Course createCourse(Course newCourse) throws ValidException, PersistenceException {
        if(newCourse.getObjectId()!=null) throw new ValidException("ObjectId must be null");

        return ctrl.createCourse(newCourse);
    }
    @PUT
    public Course updateCourse(Course updatedCourse) throws ValidException, PersistenceException {
        return ctrl.updateCourse(updatedCourse);
    }



}
