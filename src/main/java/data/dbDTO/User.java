package data.dbDTO;


import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.List;

@Data
@Entity
public class User {
	@Id
	private ObjectId userId;
	@NonNull
	private String userName;
	private List<Role> roles;

	@Embedded
	private LinkCollection generalLinks;


}
