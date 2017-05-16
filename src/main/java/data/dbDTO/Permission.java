package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
@Embedded
@NoArgsConstructor
@RequiredArgsConstructor
public class Permission {
    @NonNull
    String permissionPath;
    @NonNull
    RestMethod method;

    public enum RestMethod {
        GET, PUT,POST,DELETE,PATCH
    }
}
