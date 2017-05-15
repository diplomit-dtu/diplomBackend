package data.dbDTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;

/**
 * Created by Christian on 11-05-2017.
 */
@Entity
@Data

public class StudentAgenda {
    @Id
    ObjectId objectId;
    @Reference
    CoursePlan coursePlan;
    //TODO: Add Elements

}
