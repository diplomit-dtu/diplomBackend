package dao;

import business.impl.CourseControllerImpl;
import business.interfaces.CourseController;
import data.dbDTO.Course;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import org.junit.Test;
import rest.ValidException;

import java.time.LocalDate;

import static org.junit.Assert.fail;

/**
 * Created by Christian on 30-05-2017.
 */
public class CourseDAOTest {

    @Test
    public void  testCourse(){
        CourseController courseController= new CourseControllerImpl();

        Course newCourse = new Course();
        newCourse.setCourseId("TestCourse" +
                "");
        newCourse.setCourseName("Videregående Programmering");
        newCourse.setCoursePlanId(new ObjectId().toHexString());
        newCourse.setCoursePlanSource(Course.CoursePlanSource.GoogleSheet);
        LocalDate localDate = LocalDate.parse("2017-02-01");
        newCourse.setStartingDate(localDate);
        newCourse.setEndingDate(LocalDate.parse("2017-07-01"));
        newCourse.setLinkCollection("59241045cff47e08c4c277a3");
        try {
            Course savedCourse = courseController.createCourse(newCourse);
            System.out.println(savedCourse.getId());
            courseController.deleteCourse(savedCourse.getId());
        } catch (ValidException e) {
            e.printStackTrace();
            fail();
        } catch (PersistenceException e) {
            fail();
        }
    }
}
