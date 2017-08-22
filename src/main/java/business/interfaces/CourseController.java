package business.interfaces;

import auth.AccessDeniedException;
import data.dbDTO.*;
import data.interfaces.PersistenceException;
import data.viewDTO.CourseAddUserInfo;
import data.viewDTO.CourseNameAndShort;
import rest.ElementNotFoundException;
import rest.ValidException;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 15-05-2017.
 */
public interface CourseController {

    List<Course> getCourses();

    Course getCourse(String id) throws PersistenceException, ValidException;

    Course createCourse(Course newCourse) throws ValidException, PersistenceException;

    Course updateCourse(Course updatedCourse, ContainerRequestContext requestContext) throws PersistenceException, ValidException, AccessDeniedException;

    String deleteCourse(String id) throws PersistenceException, ValidException;



    //CoursePlans

    CoursePlan getCoursePlan(String id) throws PersistenceException, ElementNotFoundException, ValidException;

    CoursePlan getCoursePlanTemplate();

    CoursePlan createCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    CoursePlan updateCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    //GoogleCoursePlans
    CoursePlan getGoogleCoursePlan(String id) throws ValidException, PersistenceException;


    Map<String, Collection<User>> getUsers(String courseId) throws ValidException, PersistenceException;

    void addUserToCourse(String courseId, CourseAddUserInfo userRoleInfo, ContainerRequestContext requestContext) throws ValidException, PersistenceException, ElementNotFoundException, AccessDeniedException;

    void addUserToCourse(String courseID, User user, String role, ContainerRequestContext requestContext) throws PersistenceException, ValidException, AccessDeniedException;

    void addUserToCourse(String courseId, User user, ContainerRequestContext requestContext) throws ValidException, PersistenceException, ElementNotFoundException, AccessDeniedException;

    void removeUserFromCourse(String id, CourseAddUserInfo userRoleInfo, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    void updateCourseNameAndShort(String id, CourseNameAndShort courseNameAndShort, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    void updateUsesGoogleSheet(String id, Boolean usesGoogleSheet, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    void updateGoogleSheetId(String courseId, String SheetId, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    void syncCoursePlan(String id, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    List<Course> getMultiCourses(Collection<String> courseIds) throws ValidException, PersistenceException;

    Course adminAddLink(String id, EmbeddedLink link, ContainerRequestContext userFromContext) throws ValidException, PersistenceException, AccessDeniedException;

    Course adminRemoveLink(String id, EmbeddedLink link, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    Course adminUpdateLinks(String id, List<EmbeddedLink> links, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;

    List<Course> getAdminsCourses(ContainerRequestContext requestContext);

    Course adminUpdateCourseContentLines(String id, List<CourseInfoLine> courseInfos, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException;
}
