package rest;

import auth.Permission;
import auth.SecureEndpoint;
import business.interfaces.CourseController;
import data.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.PersistenceException;
import data.viewDTO.UserRoleInfo;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.ws.rs.core.Response.ok;

/**
 * Created by Christian on 15-05-2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
public class CourseService {
    CourseController courseController = ControllerRegistry.getCourseController();

    @Context
    ContainerRequestContext requestContext;


    @GET //ALL
    public List<Course> getCourses(){
        return courseController.getCourses();
    }


    @Path("{id}")
    public CourseResource getCourse(@PathParam("id") String id) throws PersistenceException, ValidException {
        return new CourseResource(id);
    }

    @PUT
    @SecureEndpoint(Permission.PORTAL_ADMIN) //Portal administrators can always fiddle
    public Course updateCourse(Course course) throws PersistenceException, ValidException, AccessDeniedException, auth.AccessDeniedException {
        return courseController.updateCourse(course);
    }


    @POST
    @SecureEndpoint(Permission.PORTAL_ADMINCOURSES)
    public Course createCourse(Course newCourse) throws ValidException, PersistenceException {
        if(newCourse.getObjectId()!=null) throw new ValidException("ObjectId must be null");

        return courseController.createCourse(newCourse);
    }
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public class CourseResource {
        private final String id;

        public CourseResource(String id) {
            this.id =id;
        }

        @GET
        public Course getCourse() throws ValidException, PersistenceException {
            return courseController.getCourse(id);
        }

        @Path("users")
        public CourseUserResource getCourseUserResource(){
            return new CourseUserResource(id);
        }


        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public class CourseUserResource {
            private final String id;

            CourseUserResource(String id) {
                this.id=id;
            }

            @GET
            public Map<Role, Set<String>> getUsersOnCourse() throws ValidException, PersistenceException {
                return courseController.getUsers(id);
            }

            @POST
            public Response addUserToCourse(UserRoleInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException, AccessDeniedException, auth.AccessDeniedException {
                //Add user to course
                courseController.addUserToCourse(id, userRoleInfo);
                return ok().entity("User Added to Course").build();
            }

            @DELETE
            public Response removeUserFromCourse(UserRoleInfo userRoleInfo) throws ValidException, PersistenceException {
                courseController.removeUserFromCourse(id,userRoleInfo);
                return Response.ok().entity("User removed from course").build();
            }


        }
    }


}
