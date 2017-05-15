package rest;

import business.impl.CourseControllerImpl;
import business.interfaces.CourseController;
import data.dbDTO.Course;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
@Path("courses")
public class CourseService {
    CourseController ctrl = new CourseControllerImpl();

    @GET
    public List<Course> getCourses(){
        return ctrl.getCourses();
    }

    @POST
    public Course createCourse(Course newCourse) throws ValidException{
        if(newCourse.getObjectId()!=null) throw new ValidException("ObjectId must be null");

        return ctrl.createCourse(newCourse);
    }

}
