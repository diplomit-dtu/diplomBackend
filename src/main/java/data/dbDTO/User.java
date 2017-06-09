package data.dbDTO;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class User extends BaseDTO{
	public static final String userNameString="userName";
	private String userName;
	private List<Role> roles;
	private Map<String, Permission> permissions = new HashMap<>();
	@Embedded
	private List<AgendaInfo> StudentAgendaInfos = new ArrayList<>();

	public User(String userName){
		this.userName=userName;
	}

	@Embedded
	private LinkCollection generalLinks;

}
