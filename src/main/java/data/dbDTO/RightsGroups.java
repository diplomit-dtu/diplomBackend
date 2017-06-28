package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Christian on 26-06-2017.
 */
@Data
@Embedded
public class RightsGroups {
    private Map<String,Set<String>> roleSetMap = new HashMap<>();

    public Set<String> getUsersInRole(Role role){
        return roleSetMap.get(role);
    }

    public void addUser(Role role, String userId) {
        if(!roleSetMap.containsKey(role)){
            roleSetMap.put(role.getRoleName(),new HashSet<>());
        }
        roleSetMap.get(role).add(userId);
    }

    public boolean removeUser(Role role, String userId){
         Set<String> strings = roleSetMap.get(role.getRoleName());
        if (strings!=null){
            return strings.remove(userId);
        } else {
            return false;
        }
    }
}
