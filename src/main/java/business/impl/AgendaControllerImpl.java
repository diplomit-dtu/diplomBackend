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
    public StudentAgenda getAgenda(String agendaId) throws ValidException, PersistenceException, ElementNotFoundException {
        StudentAgenda agenda = agendaDAO.get(agendaId);
        if (agenda == null) throw new ElementNotFoundException("Agenda not found!");
        String courseOId = agenda.getCourseOId();
        if (courseOId == null) throw new PersistenceException("courseId missing on agenda!");
        Course course = courseController.getCourse(courseOId);
        if (course==null) throw new PersistenceException("Course doesn't exist! " + courseOId);
        Course.CoursePlanSource coursePlanSource = course.getCoursePlanSource();
        CoursePlan coursePlan;
        if (Course.CoursePlanSource.GoogleSheet.equals(coursePlanSource)){
            coursePlan = courseController.getGoogleCoursePlan(course.getCoursePlanId());
        } else if (Course.CoursePlanSource.Mongo.equals(coursePlanSource)){
            coursePlan = courseController.getCoursePlan(course.getCoursePlanId());
        } else {
            throw new PersistenceException("Wrong type of CoursePlanSource: " + coursePlanSource); //Forgot to implent an enum!
        }
        agenda.setCoursePlan(coursePlan);


        return agenda ;
    }

    @Override
    public StudentAgenda saveAgenda(StudentAgenda agenda) throws PersistenceException {
        return agendaDAO.save(agenda);
    }

}
