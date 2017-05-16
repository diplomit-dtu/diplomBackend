package data.dbDTO;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class Role {
    @Id
    ObjectId objectId;
    String RoleName;

    @Embedded
    List<Permission> permissions = new ArrayList<>();

    List<ObjectId> inheritsPermissionsFromRole = new ArrayList<>();


}
