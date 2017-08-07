package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Course extends BaseDTO{
    public final static String COURSE_ID = "courseShortHand"; //For Unique identification - used for seaching
    private String courseShortHand; //ShortHandCode for Course ie. 02324F17
    private String courseName; //Course name - Advanced programming
    private LocalDate startingDate; //Start of course
    private LocalDate endingDate; //End of course
    private Set<String> tAs = new HashSet<>();
    private Set<String> students = new HashSet<>();

    //Referenced documents
    private String linkCollection;

    //Reference for fetching courseplan
    private CoursePlanSource coursePlanSource;
    private String googleSheetPlanId; // GoogleSheetID

    private String CourseplanId; //MongoDBId


    //TODO add relevant ekstra information
    public enum CoursePlanSource {
        GoogleSheet, Mongo
    }

}
