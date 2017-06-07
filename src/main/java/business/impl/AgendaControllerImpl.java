package business.impl;

import business.interfaces.AgendaController;
import data.interfaces.AgendaDAO;
import business.interfaces.CourseController;
import data.dbDTO.Course;
import data.dbDTO.CoursePlan;
import data.dbDTO.StudentAgenda;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoAgendaDAO;
import rest.ElementNotFoundException;
import rest.ValidException;

/** AgendaController - handles StudentAgendas
 * Created by Christian on 25-05-2017.
 */
public class AgendaControllerImpl implements AgendaController {
    private CourseController courseController = new CourseControllerImpl();
    private AgendaDAO agendaDAO = new MongoAgendaDAO();

    @Override
    public StudentAgenda getAgenda(String courseId, String studentId) throws ValidException, PersistenceException, ElementNotFoundException {
        Course course = courseController.getCourse(courseId);
        if (course==null){
            return null;
        }
        Course.CoursePlanSource coursePlanSource = course.getCoursePlanSource();
        CoursePlan coursePlan;
        if (Course.CoursePlanSource.GoogleSheet.equals(coursePlanSource)){
            coursePlan = courseController.getGoogleCoursePlan(course.getCoursePlanId());
        } else if (Course.CoursePlanSource.Mongo.equals(coursePlanSource)){
            coursePlan = courseController.getCoursePlan(course.getCoursePlanId());
        } else {
            throw new PersistenceException("Wrong type of CoursePlanSource: " + coursePlanSource); //Forgot to implent an enum!
        }
        StudentAgenda agenda = agendaDAO.get()

        return null ;
    }

    private StudentAgenda enrich(CoursePlan coursePlan) {


        return null;
    }
}
