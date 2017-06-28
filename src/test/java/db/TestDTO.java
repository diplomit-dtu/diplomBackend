package db;

import data.dbDTO.BaseDTO;
import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
public class TestDTO extends BaseDTO {
    @Id
    ObjectId objectId;
    String someText;
}
