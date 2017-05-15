package data.dbDTO;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class ActivityElement {
    @Id
    ObjectId objectId;

    String title;

    List<ActivitySubElement> subElements;
}
