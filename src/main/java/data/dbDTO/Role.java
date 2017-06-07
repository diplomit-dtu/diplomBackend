package data.dbDTO;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
public class Role extends BaseDTO {

    private String RoleName;

    @Embedded
    private Map<String, Permission> permissions = new HashMap<>();

    private List<String> inheritsPermissionsFromRole = new ArrayList<>();

}
