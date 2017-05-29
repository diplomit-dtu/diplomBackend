package business.interfaces;

import data.dbDTO.Course;
import data.dbDTO.CoursePlan;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
public interface CourseController {

    List<Course> getCourses();

    Course getCourse(String id) throws PersistenceException, ValidException;

    Course createCourse(Course newCourse) throws ValidException, PersistenceException;

    Course updateCourse(Course updatedCourse) throws PersistenceException;

    //CoursePlans

    CoursePlan getCoursePlan(String id) throws PersistenceException, ElementNotFoundException, ValidException;

    CoursePlan getCoursePlanTemplate();

    CoursePlan createCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    CoursePlan updateCoursePlan(CoursePlan coursePlan) throws PersistenceException;

    //GoogleCoursePlans
    CoursePlan getGoogleCoursePlan(String id) throws ValidException, PersistenceException;
}
