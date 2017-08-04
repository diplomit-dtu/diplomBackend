package business.interfaces;

import data.dbDTO.Course;
import data.dbDTO.CoursePlan;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.viewDTO.CourseAddUserInfo;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.nio.file.AccessDeniedException;
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

    Course updateCourse(Course updatedCourse) throws PersistenceException, ValidException, AccessDeniedException, auth.AccessDeniedException;

    String deleteCourse(String id) throws PersistenceException, ValidException;



    //CoursePlans

    CoursePlan getCoursePlan(String id) throws PersistenceException, ElementNotFoundException, ValidException;

    CoursePlan getCoursePlanTemplate();

    CoursePlan createCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    CoursePlan updateCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    //GoogleCoursePlans
    CoursePlan getGoogleCoursePlan(String id) throws ValidException, PersistenceException;


    Map<String, Collection<User>> getUsers(String courseId) throws ValidException, PersistenceException;

    void addUserToCourse(String courseId, CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException;

    void addUserToCourse(String courseId, User user) throws ValidException, PersistenceException, ElementNotFoundException;

    void removeUserFromCourse(String id, CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException;
}
