package data.dbDTO;


import auth.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class User extends BaseDTO{
	public static final String userNameString="userName";
	private String userName;
	private List<Role> roles = new ArrayList<>();
	private Map<String, Role> resourceSpecificRoles = new HashMap<>();
	private Set<Permission> permissions = new HashSet<>();
	private Map<String, Role> resourceSpecificPermissions = new HashMap<>();
	@Embedded
	private List<AgendaInfo> StudentAgendaInfos = new ArrayList<>();

	public User(String userName){
		this.userName=userName;
	}

	@Embedded
	private LinkCollection generalLinks;

}
