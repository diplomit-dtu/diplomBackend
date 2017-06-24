package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@Embedded
@NoArgsConstructor
public class CourseActivity {

    private String title;
    private String Description;
    private ActivityStatus status = ActivityStatus.VISIBLE;

    private Date startDate;
    private Date EndDate;

    @Embedded
    private List<CourseActivityElement> activityElementList = new ArrayList<>();

    public enum ActivityStatus{
        INVISIBLE, DRAFT, VISIBLE
    }
}
