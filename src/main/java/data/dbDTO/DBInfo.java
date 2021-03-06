package data.dbDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DBInfo {
    private transient String hostUrl;
    private String id;
    private Boolean revoked;
    private String pass;
    private transient Double size;
}
