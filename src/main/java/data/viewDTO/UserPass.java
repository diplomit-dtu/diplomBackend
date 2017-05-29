package data.viewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserPass {
	@NonNull private String username;
	@NonNull private String password;


}
