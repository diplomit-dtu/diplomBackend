package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class StudentAgenda extends BaseDTO {

    transient private CoursePlan coursePlan;
    private Map<String, SubElementMetaData> metaData = new HashMap<>();
    private List<Link> courseLinks;


}
