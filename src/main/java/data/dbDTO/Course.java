package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDate;
import java.util.*;

/** CourseDTO
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Course extends BaseDTO{
    public final static String COURSE_ID = "courseId"; //For Unique identification - used for seaching
    private String courseId; //ShortHandCode for Course ie. 02324F17
    private String courseName; //Course name - Advanced programming
    private LocalDate startingDate; //Start of course
    private LocalDate endingDate; //End of course
    private Set<String> Admins = new HashSet<>();
    private Set<String> TAs = new HashSet<>();
    private Set<String> Students = new HashSet<>();

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
