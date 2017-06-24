package rest;

import auth.Permission;
import auth.SecureEndpoint;
import business.impl.CourseControllerImpl;
import business.interfaces.CourseController;
import data.dbDTO.Course;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
public class CourseService {
    CourseController ctrl = new CourseControllerImpl();
    @Context
    ContainerRequestContext requestContext;

    @GET //ALL
    public List<Course> getCourses(){
        return ctrl.getCourses();
    }


    @Path("{id}")
    public Course getCourse(@PathParam("id") String id) throws PersistenceException, ValidException {
        return new CourseResource(id);
    }

    @PUT
    @SecureEndpoint(Permission.PORTAL_ADMIN) //Portal administrators can always fiddle
    public Course updateCourse(Course course) throws PersistenceException, ValidException, AccessDeniedException, auth.AccessDeniedException {
        return ctrl.updateCourse(course);
    }


    @POST
    @SecureEndpoint(Permission.PORTAL_ADMINCOURSES)
    public Course createCourse(Course newCourse) throws ValidException, PersistenceException {
        if(newCourse.getObjectId()!=null) throw new ValidException("ObjectId must be null");

        return ctrl.createCourse(newCourse);
    }

    public class CourseResource extends Course {
        private final String id;

        public CourseResource(String id) {
            this.id =id;
        }

        @GET
        public Course getCourse() throws ValidException, PersistenceException {
            return ctrl.getCourse(id);
        }


    }


}
