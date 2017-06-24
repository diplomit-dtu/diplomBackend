package data.dbDTO;

import auth.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import java.util.*;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Role extends BaseDTO {

    public static final String ROLE_NAME = "roleName";
    private String roleName;

    @Embedded
    private Set<Permission> permissions = new HashSet<>();

    private List<Role> inheritsPermissionsFromRoles = new ArrayList<>();

}
