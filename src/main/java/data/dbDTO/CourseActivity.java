package data.dbDTO;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class CourseActivity {
    @Id
    private ObjectId objectId;

    private String Description;

    private LocalDate startDate;
    private LocalDate EndDate;

    private List<ActivityElement> activityElementList;

    public enum ActivityStatus{
        INVISIBLE, DRAFT, VISIBLE
    }
}
