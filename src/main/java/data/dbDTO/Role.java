package data.dbDTO;

import auth.Permission;
import dev.morphia.annotations.Embedded;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Role(String roleName) {
        this.roleName=roleName;
    }
}
