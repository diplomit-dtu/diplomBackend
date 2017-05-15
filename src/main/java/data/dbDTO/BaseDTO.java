package data.dbDTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
@Entity
public class BaseDTO {
    @Id
    ObjectId objectId;
}
