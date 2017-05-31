package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@NoArgsConstructor
public class CoursePlan extends BaseDTO {

    private String googleSheetId;

    @Embedded
    List<CourseActivity> courseActivityList = new ArrayList<>();


}
