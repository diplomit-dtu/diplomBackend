package business.impl;

import business.interfaces.AgendaController;
import business.interfaces.CourseController;
import business.interfaces.CoursePlanController;
import business.interfaces.UserController;
import business.ControllerRegistry;
import data.dbDTO.*;
import data.googleImpl.GoogleCoursePlanDAO;
import data.interfaces.CourseDAO;
import data.interfaces.CoursePlanDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoCourseDAO;
import data.mongoImpl.MongoCoursePlanDAO;
import data.viewDTO.CourseAddUserInfo;
import data.viewDTO.CourseNameAndShort;
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
    public static Map<String, CoursePlan> cachedCoursePlans = new HashMap<>();

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

        if (course.getCourseShortHand()==null || courseDAO.findCourseByCourseID(course.getCourseShortHand())==null) {
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
        if (cachedCoursePlans.get(id)==null) {
            cachedCoursePlans.put(id, mongoCoursePlanDAO.get(id));
        }
        if (cachedCoursePlans.get(id)==null) throw new ElementNotFoundException("CoursePlan notFound: " + id);
        return cachedCoursePlans.get(id);
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
    public Map<String, Collection<User>> getUsers(String courseId) throws ValidException, PersistenceException {
        Course course = getCourse(courseId);
        System.out.println("courseAdmins: " + course.getAdmins());
        UserController userController = ControllerRegistry.getUserController();
        Collection<User> admins = userController.getMultiple(course.getAdmins());
        System.out.println(admins);
        Collection<User> students = userController.getMultiple(course.getStudents());
        Collection<User> tas = userController.getMultiple(course.getTAs());
        Map<String, Collection<User>> userMap = new HashMap<>();
        userMap.put("students", students);
        userMap.put("admins", admins);
        userMap.put("tas", tas);
        return userMap;
    }

    @Override
    public void addUserToCourse(String id, CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException, ElementNotFoundException {
        UserController userController = ControllerRegistry.getUserController();
        Course course = getCourse(id);
        User user = null;
        if (userRoleInfo.getUserId()!=null) {
            //user identified by objectId
            user = userController.get(userRoleInfo.getUserId());
            if (user == null) {
                throw new ElementNotFoundException("user not found");
            } else if (course == null) {
                throw new ElementNotFoundException("course not found");
            }
        } else if (userRoleInfo.getUserName()!=null) {
            // Add by userName
            try {
                user = userController.getUserByCampusNetId(userRoleInfo.getUserName());
            } catch (ElementNotFoundException e){
                //User is not in db
                user = new User();
                user.setUserName(userRoleInfo.getUserName());
                user.setFirstName(userRoleInfo.getName());
                userController.saveUser(user);
            }

        }

            String role = userRoleInfo.getRole().getRoleName().toLowerCase();
            modifyCourse(user.getId(), course, role);

            modifyUserAndCreateAgenda(course, user);
            //TODO could rewrite for two phase commmit - but some orphan data doesn't matter
            updateCourse(course);
            userController.saveUser(user);

        }

    @Override
    public void addUserToCourse(String courseID, User user, String role) throws PersistenceException, ValidException {
        UserController userController = ControllerRegistry.getUserController();
        Course course = getCourse(courseID);
        if (user.getId()!=null){
            modifyUserAndCreateAgenda(course, user);
            userController.saveUser(user);
        } else if(user.getUserName()!=null){
            User userByCampusNetId = null;
            try {
                userByCampusNetId = userController.getUserByCampusNetId(user.getUserName());
            } catch (ElementNotFoundException e) {
                user = userController.saveUser(user);
            }
            if (userByCampusNetId!=null){//User alreadyExists
                user = userByCampusNetId;
            }
            modifyUserAndCreateAgenda(course, user);
        } else {
            return;
        }
        modifyCourse(user.getId(), course, role);
        updateCourse(course);
        userController.saveUser(user);
    }

    @Override
        public void addUserToCourse(String id, User user) throws ValidException, PersistenceException {
            addUserToCourse(id,user,"student");

        }

        @Override
        public void removeUserFromCourse(String id, CourseAddUserInfo userRoleInfo) throws ValidException, PersistenceException {
            UserController userController = ControllerRegistry.getUserController();
            Course course = getCourse(id);
            User user = userController.get(userRoleInfo.getUserId());
            course.getStudents().remove(userRoleInfo.getUserId());
            Map<String, AgendaInfo> studentAgendaInfos = user.getAgendaInfoMap();
            if (studentAgendaInfos!=null && studentAgendaInfos.get(id)!=null) {
                if (studentAgendaInfos.get(id)!=null)ControllerRegistry.getAgendaController().deleteAgenda(studentAgendaInfos.get(id).getAgendaId());
                studentAgendaInfos.remove(id);
            }
            userController.saveUser(user);
            updateCourse(course);
        }

    @Override
    public void updateCourseNameAndShort(String id, CourseNameAndShort courseNameAndShort) throws ValidException, PersistenceException {
        Course course = getCourse(id);
        course.setCourseName(courseNameAndShort.getCourseName());
        course.setCourseShortHand(courseNameAndShort.getShortHand());
        updateCourse(course);
    }

    @Override
    public void updateUsesGoogleSheet(String id, Boolean usesGoogleSheet) throws ValidException, PersistenceException {
        Course course  = getCourse(id);
        course.setCoursePlanSource(usesGoogleSheet ? Course.CoursePlanSource.GoogleSheet: Course.CoursePlanSource.Mongo);
        updateCourse(course);
    }

    @Override
    public void updateGoogleSheetId(String courseId, String sheetId) throws ValidException, PersistenceException {
        Course course = getCourse(courseId);
        course.setGoogleSheetPlanId(sheetId);
        updateCourse(course);
    }

    @Override
    public void syncCoursePlan(String id) throws ValidException, PersistenceException {
        CoursePlanController coursePlanController = ControllerRegistry.getCoursePlanController();

        Course course = getCourse(id);
        String googleSheetPlanId = course.getGoogleSheetPlanId();
        if (googleSheetPlanId ==null || googleSheetPlanId==""){
            throw new ValidException("Course has no google Sheet ID");
        }
        if (course.getCourseplanId()==null){
            CoursePlan coursePlan = coursePlanController.createCoursePlan();
            course.setCourseplanId(coursePlan.getId());
        }
        updateCourse(course);
        coursePlanController.syncCoursePlan(googleSheetPlanId, course.getCourseplanId());
        //Clear cache
        cachedCoursePlans.remove(course.getCourseplanId());
    }

    private void modifyUserAndCreateAgenda(Course course, User user) throws ValidException, PersistenceException {
        Map<String, AgendaInfo> studentAgendaInfos = user.getAgendaInfoMap();
        if (studentAgendaInfos==null){
            user.setAgendaInfoMap(new HashMap<>());
            studentAgendaInfos = user.getAgendaInfoMap();
        }
        if (!studentAgendaInfos.containsKey(course.getId())) {
            AgendaInfo agendaInfo = new AgendaInfo();
            agendaInfo.setCourseName(course.getCourseName());
            AgendaController agendaController = ControllerRegistry.getAgendaController();
            AgendaInfo newAgenda = agendaController.createNewAgenda(agendaInfo, user.getId());
            studentAgendaInfos.put(course.getId(), newAgenda);
        }
    }

    private void modifyCourse(String userId, Course course, String role) {
        if("admin".equals(role)){
            course.getAdmins().add(userId);
        } else if ("student".equals(role)){
            course.getStudents().add(userId);
        } else if ("ta".equals(role)){
            course.getTAs().add(userId);
        } else {
            course.getRightsGroups().addUser(new Role(role),userId);
        }
    }


}
