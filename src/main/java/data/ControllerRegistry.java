package data;

import business.impl.AgendaControllerImpl;
import business.impl.CourseControllerImpl;
import business.impl.UserControllerImpl;
import business.interfaces.AgendaController;
import business.interfaces.CourseController;
import business.interfaces.UserController;

/**
 * Created by Christian on 27-06-2017.
 */
public class ControllerRegistry {

    private static CourseController courseController;
    private static AgendaController agendaController;
    private static UserController userController;

    public static CourseController getCourseController(){
        if (courseController==null) courseController=new CourseControllerImpl();
        return courseController;
    }
    public static AgendaController getAgendaController(){
        if (agendaController==null) agendaController = new AgendaControllerImpl();
        return agendaController;
    }

    public static UserController getUserController(){
        if (userController == null) userController = new UserControllerImpl();
        return userController;
    }

}
