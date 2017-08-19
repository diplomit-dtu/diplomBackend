package data.dbDTO;


import auth.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class User extends BaseDTO{
	public static final String userNameString="userName";

	@Indexed
	private String userName;
	private String Email;
	private String firstName;
	private String lastName;
	private Set<Permission> permissions = new HashSet<>();
	private List<Role> roles = new ArrayList<>();
	private transient boolean isAdminOfCourses = false;


	@Embedded
	private Map<String, AgendaInfo> agendaInfoMap = new HashMap<>(); //courseId,<AgendaId,CourseName>
	private String activeAgenda;
	@Embedded
	private LinkCollection generalLinks;

	private String UserDataId; //non-permission data

	public User(String userName){
		this.userName=userName;
	}


}
