package data.dbDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDate;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@Entity
@RequiredArgsConstructor
public class Course extends BaseDTO{
    public final static String COURSE_ID = "courseId";
    private String courseId; //ShortHandCode for Course ie. 02324F17
    private String courseName; //Course name - Advanced programming
    private LocalDate startingDate; //Start of course
    private LocalDate endingDate; //End of course

    //Referenced documents
    private String linkCollection;

    //Reference for fetching courseplan

    private CoursePlanSource coursePlanSource;
    private String coursePlanId;

    //TODO add relevant ekstra information
    public enum CoursePlanSource {
        GoogleSheet, Mongo
    }

}
