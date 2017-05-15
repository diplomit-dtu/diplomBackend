package data.dbDTO;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 11-05-2017.
 */
public class ActivitySubElement {
    @Id
    ObjectId objectId;
}
