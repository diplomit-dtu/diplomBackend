package rest;

import auth.AuthorizationFilter;
import business.impl.GoogleSheetParser;
import business.interfaces.CourseController;
import business.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.PersistenceException;
import data.viewDTO.CSVUserString;
import data.viewDTO.CourseAddUserInfo;
import data.viewDTO.CourseNameAndShort;
import data.viewDTO.ViewUserItem;
import util.CSVParser;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.AccessDeniedException;
import java.util.*;

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
    public CourseIdResource getCourse(@PathParam("id") String id) throws PersistenceException, ValidException {
        return new CourseIdResource(id);
    }

    @PUT
    public Course updateCourse(Course course) throws PersistenceException, ValidException, AccessDeniedException, auth.AccessDeniedException {
        return courseController.updateCourse(course);
    }


    @POST
    public Course createCourse(Course newCourse) throws ValidException, PersistenceException {
        if (newCourse ==null) {
            newCourse = new Course();
        }
        if (newCourse.getCourseName()==null){
            newCourse.setCourseName("Nyt Kursus");
        }

        User user = userFromContext();
        newCourse.getAdmins().add(user.getId());
        if(newCourse.getObjectId()!=null) throw new ValidException("ObjectId must be null");


        Course course = courseController.createCourse(newCourse);
        courseController.addUserToCourse(course.getId(),user, "admin");

        return course;
    }

    private User userFromContext() {
        Object userProperty = requestContext.getProperty(AuthorizationFilter.USER);
        if (userProperty instanceof User){
            return (User) userProperty;
        }
        return null;
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public class CourseIdResource {
        private final String id;

        public CourseIdResource(String id) {
            this.id =id;
        }

        @GET
        public Course getCourse() throws ValidException, PersistenceException {
            return courseController.getCourse(id);
        }

        @POST
        @Path("name")
        public void updateCourseNameAndShortHand(CourseNameAndShort courseNameAndShort) throws ValidException, PersistenceException {
            courseController.updateCourseNameAndShort(id, courseNameAndShort);

        }
        @POST
        @Path("usesGoogleSheet")
        public void updateUsesGoogleSheet(HashMap<String,Boolean> map) throws ValidException, PersistenceException {
            Boolean usesGoogleSheet = map.get("usesGoogleSheet");
            courseController.updateUsesGoogleSheet(id, usesGoogleSheet);
        }

        @POST
        @Path("googleSheetId")
        public void updateGoogleSheetId(HashMap<String, String> map) throws ValidException, PersistenceException {
            String googleSheetId = map.get("googleSheetId");
            try {
                googleSheetId = GoogleSheetParser.parseLinkForSheetId(googleSheetId);
            } catch (GoogleSheetParser.IdNotFoundException e) {
                //Assume that an ID was posted
            }
            courseController.updateGoogleSheetId(id,googleSheetId);
        }

        @POST
        @Path("syncCoursePlan")
        public void syncCoursePlan() throws ValidException, PersistenceException {
            courseController.syncCoursePlan(id);
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
            public Map<String, Collection<User>> getUsersOnCourse() throws ValidException, PersistenceException {
                return courseController.getUsers(id);
            }

            @GET
            @Path("list")
            public Collection<ViewUserItem> getViewUserList() throws ValidException, PersistenceException {
                Map<String,ViewUserItem> map = new HashMap<>();
                Map<String, Collection<User>> users = courseController.getUsers(id);
                Collection<User> admins = users.get("admins");
                for (User user : admins){
                    map.put(user.getId(),new ViewUserItem(user.getId(),user.getUserName(),user.getFirstName(),user.getLastName(),user.getEmail(),true,false,false));
                }
                for(User user : users.get("students")){
                    ViewUserItem viewUserItem = map.get(user.getId());
                    if (viewUserItem!=null){
                        viewUserItem.setStudent(true);
                    } else {
                        map.put(user.getId(),new ViewUserItem(user.getId(),user.getUserName(),user.getFirstName(),user.getLastName(),user.getEmail(),false,true,false));
                    }
                }
                for(User user : users.get("tas")){
                    ViewUserItem viewUserItem = map.get(user.getId());
                    if (viewUserItem!=null){
                        viewUserItem.setTa(true);
                    } else {
                        map.put(user.getId(),new ViewUserItem(user.getId(),user.getUserName(),user.getFirstName(),user.getLastName(),user.getEmail(),false,false,true));
                    }
                }
                return map.values();
            }

            @POST
            public Boolean addUserToCourse(CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException, AccessDeniedException, auth.AccessDeniedException {
                courseController.addUserToCourse(id, userRoleInfo);
                return true;
            }

            @POST
            @Path("csv")
            public Boolean addCSVAddUsersToCourse(CSVUserString csv) throws ValidException, ElementNotFoundException, PersistenceException {
                String csvString = csv.getUsersCsv();
                List<User> usersFromCsv = CSVParser.getUsersFromCsv(csvString);
                for (User u: usersFromCsv){
                    courseController.addUserToCourse(id, u);
                }
                return true;
            }

            @PUT
            @Path("{id}/{role}")
            public boolean updateUserRole(@PathParam("id") String userId, @PathParam("role") String role, Boolean hasRole) throws ValidException, PersistenceException, AccessDeniedException, auth.AccessDeniedException {
                Course course = courseController.getCourse(id);
                Set<String> userSet;
                if ("admin".equals(role)){
                    userSet = course.getAdmins();
                } else if ("student".equals(role)){
                    userSet = course.getStudents();
                } else if ("ta".equals(role)){
                    userSet = course.getTAs();
                } else {
                    return false;
                }
                if (hasRole){
                    userSet.add(userId);
                } else {
                    userSet.remove(userId);
                }
                //Check if user still is in course
                if (!(course.getAdmins().contains(userId) || course.getTAs().contains(userId) || course.getStudents().contains(userId))) {
                    ControllerRegistry.getUserController().removeAgenda(userId, id);

                }
                courseController.updateCourse(course);
                return true;

            }

            @DELETE
            public Response removeUserFromCourse(CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException {
                courseController.removeUserFromCourse(id,userRoleInfo);
                return Response.ok().entity("User removed from course").build();
            }




        }
    }


}
