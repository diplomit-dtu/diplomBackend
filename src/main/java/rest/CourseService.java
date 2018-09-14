package rest;

import auth.AccessDeniedException;
import auth.AuthorizationFilter;
import business.ControllerRegistry;
import business.impl.GoogleSheetParser;
import business.interfaces.CourseController;
import business.interfaces.ValidException;
import data.dbDTO.Course;
import data.dbDTO.CourseInfoLine;
import data.dbDTO.EmbeddedLink;
import data.dbDTO.User;
import data.interfaces.ElementNotFoundException;
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
import java.util.*;

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


    @GET //ALL //No secrects here - could be a secure endpoint
    public List<Course> getCourses(){
        return courseController.getCourses();
    }

    @GET
    @Path("owned")
    public List<Course> getCoursesUserAdmins(){
        return courseController.getAdminsCourses(requestContext);
    }
    {

    }

    @Path("{id}")
    public CourseIdResource getCourse(@PathParam("id") String id) throws PersistenceException, ValidException {
        return new CourseIdResource(id);
    }

    @PUT
    public Course updateCourse(Course course) throws PersistenceException, ValidException, AccessDeniedException {
        return courseController.updateCourse(course, requestContext);
    }


    @POST
    public Course createCourse(Course newCourse) throws ValidException, PersistenceException, AccessDeniedException {
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
        courseController.addUserToCourse(course.getId(),user, "admin", requestContext);

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
        public void updateCourseNameAndShortHand(CourseNameAndShort courseNameAndShort) throws ValidException, PersistenceException, AccessDeniedException {
            courseController.updateCourseNameAndShort(id, courseNameAndShort, requestContext);

        }

        @POST
        @Path("addlink")
        public Course addLinkToCourse(EmbeddedLink link) throws ValidException, PersistenceException, AccessDeniedException {

            return courseController.adminAddLink(id, link, requestContext);
        }

        @POST
        @Path("removelink")
        public Course removeLinkFromCourse (EmbeddedLink link) throws ValidException, AccessDeniedException, PersistenceException {
            return courseController.adminRemoveLink(id, link, requestContext);
        }

        @PUT
        @Path("links")
        public Course updateLinks(List<EmbeddedLink> links) throws ValidException, AccessDeniedException, PersistenceException {
            return courseController.adminUpdateLinks(id,links,requestContext);
        }

        @PUT
        @Path("courseinfo")
        public Course updateCourseInfo(List<CourseInfoLine> courseInfos) throws ValidException, AccessDeniedException, PersistenceException {
            return courseController.adminUpdateCourseContentLines(id,courseInfos,requestContext);
        }

        @POST
        @Path("usesGoogleSheet")
        public void updateUsesGoogleSheet(HashMap<String,Boolean> map) throws ValidException, PersistenceException, AccessDeniedException {
            Boolean usesGoogleSheet = map.get("usesGoogleSheet");
            courseController.updateUsesGoogleSheet(id, usesGoogleSheet, requestContext);
        }

        @POST
        @Path("googleSheetId")
        public void updateGoogleSheetId(HashMap<String, String> map) throws ValidException, PersistenceException, AccessDeniedException {
            String googleSheetId = map.get("googleSheetId");
            try {
                googleSheetId = GoogleSheetParser.parseLinkForSheetId(googleSheetId);
            } catch (GoogleSheetParser.IdNotFoundException e) {
                //Assume that an ID was posted
            }
            courseController.updateGoogleSheetId(id,googleSheetId, requestContext);
        }

        @POST
        @Path("syncCoursePlan")
        public void syncCoursePlan() throws ValidException, PersistenceException, AccessDeniedException {
            courseController.syncCoursePlan(id, requestContext);
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
            public Boolean addUserToCourse(CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException, AccessDeniedException, AccessDeniedException {
                courseController.addUserToCourse(id, userRoleInfo, requestContext);
                return true;
            }

            @POST
            @Path("csv")
            public Boolean addCSVAddUsersToCourse(CSVUserString csv) throws ValidException, ElementNotFoundException, PersistenceException, AccessDeniedException {
                String csvString = csv.getUsersCsv();
                List<User> usersFromCsv = CSVParser.getUsersFromCsv(csvString);
                for (User u: usersFromCsv){
                    courseController.addUserToCourse(id, u, requestContext);
                }
                return true;
            }

            @PUT
            @Path("{id}/{role}")
            public boolean updateUserRole(@PathParam("id") String userId, @PathParam("role") String role, Boolean hasRole) throws ValidException, PersistenceException, AccessDeniedException, AccessDeniedException {
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
                courseController.updateCourse(course, requestContext);
                return true;

            }

            @DELETE
            public Response removeUserFromCourse(CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException, AccessDeniedException {
                courseController.removeUserFromCourse(id,userRoleInfo , requestContext);
                return Response.ok().entity("User removed from course").build();
            }




        }
    }


}
