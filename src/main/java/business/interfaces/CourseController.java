package business.interfaces;

import data.dbDTO.Course;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
public interface CourseController {

    List<Course> getCourses();

    Course createCourse(Course newCourse) throws ValidException;
}
