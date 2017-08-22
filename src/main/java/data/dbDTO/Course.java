package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;
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

    private Boolean archived = false;
    private LocalDate archiveDate;

    @Embedded
    private List<EmbeddedLink> courseLinks = new ArrayList<>();

    @Embedded List<CourseInfoLine> courseInfoLines = new ArrayList<>();

    //Reference for fetching courseplan
    private CoursePlanSource coursePlanSource;
    private String googleSheetPlanId; // GoogleSheetID

    private String courseplanId; //MongoDBId


    //TODO add relevant ekstra information
    public enum CoursePlanSource {
        GoogleSheet, Mongo
    }

}
