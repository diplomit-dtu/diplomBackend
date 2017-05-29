package data.interfaces;

import data.dbDTO.Course;

/**
 * Created by Christian on 15-05-2017.
 */
public interface CourseDAO extends BaseDAO<Course> {
    Course findCourseByCourseID(String CourseId) throws PersistenceException;
}
