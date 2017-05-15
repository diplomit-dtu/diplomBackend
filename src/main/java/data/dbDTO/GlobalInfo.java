package data.dbDTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Entity
@Data
public class GlobalInfo {
    @Id
    ObjectId objectId;

    List<Course> courses;
    ObjectId generalLinkCollection;


}
