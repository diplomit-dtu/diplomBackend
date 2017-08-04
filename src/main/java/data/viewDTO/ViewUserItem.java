package data.viewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Christian on 03-08-2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewUserItem {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean admin;
    private Boolean student;
    private Boolean ta;
}
