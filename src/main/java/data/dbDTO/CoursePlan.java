package data.dbDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@RequiredArgsConstructor
@Entity
public class CoursePlan {
    @Id
    ObjectId objectId;
    @Embedded
    List<CourseActivity> courseActivityList;

}
