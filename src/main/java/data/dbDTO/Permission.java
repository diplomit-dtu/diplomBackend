package data.dbDTO;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
@Embedded
public class Permission {

    String permissionPath;
    @NonNull
    RestMethod method;

}
