package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@NoArgsConstructor
public class CoursePlan extends BaseDTO {
    @Embedded
    List<CourseActivity> courseActivityList = new ArrayList<>();


}
