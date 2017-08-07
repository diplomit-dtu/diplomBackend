package business;

import business.impl.*;
import business.interfaces.*;

/**
 * Created by Christian on 27-06-2017.
 */
public class ControllerRegistry {

    private static CourseController courseController;
    private static CoursePlanController coursePlanControlller;
    private static AgendaController agendaController;
    private static UserController userController;
    private static UserDataController userDataController;

    public static CourseController getCourseController(){
        if (courseController==null) courseController=new CourseControllerImpl();
        return courseController;
    }

    public static CoursePlanController getCoursePlanController(){
        if (coursePlanControlller == null) coursePlanControlller = new CoursePlanControllerImpl();
        return coursePlanControlller;
    }

    public static AgendaController getAgendaController(){
        if (agendaController==null) agendaController = new AgendaControllerImpl();
        return agendaController;
    }

    public static UserController getUserController(){
        if (userController == null) userController = new UserControllerImpl();
        return userController;
    }

    public static UserDataController getUserDataController() {
        if (userDataController == null) userDataController = new UserDataControllerImpl();
        return userDataController;

    }
}
