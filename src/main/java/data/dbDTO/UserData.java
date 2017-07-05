package data.dbDTO;

import auth.Permission;
import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

import java.util.*;

/**
 * Created by Christian on 05-07-2017.
 */
@Data
public class UserData extends BaseDTO {
    private String firstName;
    private String lastName;
    private Map<String, Role> resourceSpecificRoles = new HashMap<>();

    private Map<String, Role> resourceSpecificPermissions = new HashMap<>();
    @Embedded
    private Map<String, AgendaInfo> agendaInfoMap = new HashMap<>(); //courseId,<AgendaId,CourseName>
    private String activeAgenda;
    @Embedded
    private LinkCollection generalLinks;
}
