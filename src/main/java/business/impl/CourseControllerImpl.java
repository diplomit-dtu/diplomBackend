package business.impl;

import business.interfaces.CourseController;
import data.dbDTO.Course;
import data.interfaces.CourseDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoCourseDAO;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
public class CourseControllerImpl implements CourseController {
    CourseDAO courseDAO = new MongoCourseDAO();

    @Override
    public List<Course> getCourses(){
        try {
            return courseDAO.getAll();
        } catch (PersistenceException e) {
            //TODO implement!!
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Course createCourse(Course course) throws ValidException {
        try {
            if (courseDAO.findCourseByCourseID(course.getCourseId())==null) {
                return courseDAO.save(course);
            } else {
                throw new ValidException("CourseID already exists");
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return null;
    }

}
