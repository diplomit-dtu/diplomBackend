package data.dbDTO;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
public class ActivitySubElement {
    @Id
    ObjectId objectId;
    @NonNull
    String title;


}
