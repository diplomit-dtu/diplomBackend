package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
@Embedded
@NoArgsConstructor
@RequiredArgsConstructor
public class Permission {

    String permissionPath;
    Set<RestMethod> allowedMethods = new HashSet<>();
    Set<RestMethod> allowedOwnMethods = new HashSet<>();
    Boolean accessControl;
    Map<String, Permission> subPermissions = new HashMap<>();

    public enum RestMethod {
        GET, PUT,POST,DELETE,PATCH, ALL
    }
}
