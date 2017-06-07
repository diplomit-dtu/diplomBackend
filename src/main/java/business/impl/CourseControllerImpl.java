package business.impl;

import business.interfaces.CourseController;
import data.dbDTO.ActivityElement;
import data.dbDTO.Course;
import data.dbDTO.CourseActivity;
import data.dbDTO.CoursePlan;
import data.googleImpl.GoogleCoursePlanDAO;
import data.interfaces.CourseDAO;
import data.interfaces.CoursePlanDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoCourseDAO;
import data.mongoImpl.MongoCoursePlanDAO;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.util.Calendar;
import java.util.List;

/** Business Logic layer for Courses.
 * Created by Christian on 15-05-2017.
 */
public class CourseControllerImpl implements CourseController {
    //TODO refactor for serviceRegistry
    private CourseDAO courseDAO = new MongoCourseDAO();
    private CoursePlanDAO mongoCoursePlanDAO = new MongoCoursePlanDAO();
    private CoursePlanDAO googleCoursePlanDAO = new GoogleCoursePlanDAO();

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
    public Course getCourse(String id) throws PersistenceException, ValidException {
        return courseDAO.get(id);
    }



    @Override
    public Course createCourse(Course course) throws ValidException, PersistenceException {
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

    @Override
    public Course updateCourse(Course updatedCourse) throws PersistenceException{
        return courseDAO.save(updatedCourse);

    }

    @Override
    public String deleteCourse(String id) throws PersistenceException, ValidException {
        if (courseDAO.delete(id)){
            return id;
        } else {
            return null;
        }

    }

    @Override
    public CoursePlan getCoursePlan(String id) throws PersistenceException, ElementNotFoundException, ValidException {
        CoursePlan coursePlan = mongoCoursePlanDAO.get(id);
        if (coursePlan==null) throw new ElementNotFoundException("CoursePlan notFound: " + id);
        return coursePlan;
    }

    @Override
    public CoursePlan getCoursePlanTemplate() {
        CoursePlan coursePlan = new CoursePlan();
        CourseActivity courseActivity = new CourseActivity();
        courseActivity.setDescription("Forberedelse");
        courseActivity.setStartDate(Calendar.getInstance().getTime());
        List<ActivityElement> activityElementList = courseActivity.getActivityElementList();
        ActivityElement activityElement = new ActivityElement();
        activityElement.setTitle("Lektion 1");
        activityElementList.add(activityElement);

        coursePlan.getCourseActivityList().add(courseActivity);
        return coursePlan;
    }

    @Override
    public CoursePlan createCoursePlan(CoursePlan coursePlan) throws PersistenceException {
        return mongoCoursePlanDAO.save(coursePlan);
    }

    @Override
    public CoursePlan updateCoursePlan(CoursePlan coursePlan) throws PersistenceException {
        return mongoCoursePlanDAO.save(coursePlan);
    }

    @Override
    public CoursePlan getGoogleCoursePlan(String id) throws ValidException, PersistenceException {
        return googleCoursePlanDAO.get(id);
    }


}
