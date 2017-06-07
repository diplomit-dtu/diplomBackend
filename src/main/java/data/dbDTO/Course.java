package data.dbDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDate;

/** CourseDTO
 * Created by Christian on 11-05-2017.
 */
@Data
@Entity
public class Course extends BaseDTO{
    public final static String COURSE_ID = "courseId"; //For Unique identification - used for seaching
    private String courseId; //ShortHandCode for Course ie. 02324F17
    private String courseName; //Course name - Advanced programming
    private LocalDate startingDate; //Start of course
    private LocalDate endingDate; //End of course

    //Referenced documents
    private String linkCollection;

    //Reference for fetching courseplan
    private CoursePlanSource coursePlanSource;
    private String coursePlanId; //Could both be GoogleSheetId and MongoDbObjectId

    //TODO add relevant ekstra information
    public enum CoursePlanSource {
        GoogleSheet, Mongo
    }

}
