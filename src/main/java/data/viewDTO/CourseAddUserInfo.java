package data.viewDTO;

import data.dbDTO.Role;
import lombok.Data;

/**
 * Created by Christian on 26-06-2017.
 */
@Data
public class CourseAddUserInfo {
    private Role role;
    private String userName;
    private String userId;
    private String email;
    private String name;

}
