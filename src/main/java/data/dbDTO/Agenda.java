package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Agenda extends BaseDTO {

    private String courseId;
    transient private CoursePlan coursePlan;
    private Map<String, SubElementMetaData> metaData = new HashMap<>(); //SubelementID, data
    private List<Link> courseLinks;


}
