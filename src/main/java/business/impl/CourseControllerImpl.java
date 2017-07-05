package business.impl;

import business.interfaces.AgendaController;
import business.interfaces.CourseController;
import business.interfaces.UserController;
import business.interfaces.UserDataController;
import data.ControllerRegistry;
import data.dbDTO.*;
import data.googleImpl.GoogleCoursePlanDAO;
import data.interfaces.CourseDAO;
import data.interfaces.CoursePlanDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoCourseDAO;
import data.mongoImpl.MongoCoursePlanDAO;
import data.viewDTO.UserRoleInfo;
import rest.ElementNotFoundException;
import rest.ValidException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.util.*;

/** Business Logic layer for Courses.
 * Created by Christian on 15-05-2017.
 */
public class CourseControllerImpl implements CourseController {
    //TODO refactor for serviceRegistry
    private CourseDAO courseDAO = new MongoCourseDAO();
    private CoursePlanDAO mongoCoursePlanDAO = new MongoCoursePlanDAO();
    private CoursePlanDAO googleCoursePlanDAO = new GoogleCoursePlanDAO();

    @Context
    ContainerRequestContext requestContext;

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

        if (courseDAO.findCourseByCourseID(course.getCourseShortHand())==null) {
            return courseDAO.save(course);
        } else {
            throw new ValidException("Course with same Shorthand code already exists");
        }
    }

    @Override
    public Course updateCourse(Course updatedCourse) throws PersistenceException, ValidException {
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
        List<CourseActivityElement> activityElementList = courseActivity.getActivityElementList();
        CourseActivityElement activityElement = new CourseActivityElement();
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

    @Override
    public Map<Role, Set<String>> getUsers(String id) throws ValidException, PersistenceException {
        Course course = getCourse(id);
        Map<Role, Set<String>> userMap = new HashMap<>();
        userMap.put(new Role("students"), course.getStudents());
        userMap.put(new Role("admins"), course.getAdmins());
        userMap.put(new Role("tas"), course.getTAs());
        return userMap;
    }

    @Override
    public void addUserToCourse(String id, UserRoleInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException {
        UserController userController = ControllerRegistry.getUserController();
        UserDataController userDataController = ControllerRegistry.getUserDataController();
        Course course = getCourse(id);
        User user = userController.get(userRoleInfo.getUserId());
        if (user ==null) {
            throw new ElementNotFoundException("user not found");
        } else if (course==null){
            throw new ElementNotFoundException("course not found");
        }
        UserData userData = userDataController.getUserData(user.getUserDataId());

        String role = userRoleInfo.getRole().getRoleName().toLowerCase();
        modifyCourse(userRoleInfo, course, role);

        modifyUserAndCreateAgenda(userRoleInfo, course, user);
        //TODO could rewrite for two phase commmit - but some orphan data doesn't matter
        updateCourse(course);

    }

    @Override
    public void removeUserFromCourse(String id, UserRoleInfo userRoleInfo) throws ValidException, PersistenceException {
        UserController userController = ControllerRegistry.getUserController();
        Course course = getCourse(id);
        User user = userController.get(userRoleInfo.getUserId());
        course.getStudents().remove(userRoleInfo.getUserId());
        UserData userData = ControllerRegistry.getUserDataController().getUserData(user.getUserDataId());
        Map<String, AgendaInfo> studentAgendaInfos = userData.getAgendaInfoMap();
        if (studentAgendaInfos!=null && studentAgendaInfos.get(id)!=null) {
            if (studentAgendaInfos.get(id)!=null)ControllerRegistry.getAgendaController().deleteAgenda(studentAgendaInfos.get(id).getAgendaId());
            studentAgendaInfos.remove(id);
        }
        userController.saveUser(user);
        updateCourse(course);
    }

    private void modifyUserAndCreateAgenda(UserRoleInfo userRoleInfo, Course course, User user) throws ValidException, PersistenceException {
        UserData userData = ControllerRegistry.getUserDataController().getUserData(user.getUserDataId());

        Map<String, AgendaInfo> studentAgendaInfos = userData.getAgendaInfoMap();
        if (!studentAgendaInfos.containsKey(course.getId())) {
            AgendaInfo agendaInfo = new AgendaInfo();
            agendaInfo.setCourseName(course.getCourseName());
            AgendaController agendaController = ControllerRegistry.getAgendaController();
            AgendaInfo newAgenda = agendaController.createNewAgenda(agendaInfo, userRoleInfo.getUserId());
            studentAgendaInfos.put(course.getId(), newAgenda);
        }
    }

    private void modifyCourse(UserRoleInfo userRoleInfo, Course course, String role) {
        String id = userRoleInfo.getUserId();
        if("admin".equals(role)){
            course.getAdmins().add(id);
        } else if ("student".equals(role)){
            course.getStudents().add(id);
        } else if ("ta".equals(role)){
            course.getTAs().add(id);
        } else {
            course.getRightsGroups().addUser(userRoleInfo.getRole(),id);
        }
    }


}
