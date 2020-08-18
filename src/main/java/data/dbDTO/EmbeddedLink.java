package data.dbDTO;

import dev.morphia.annotations.Embedded;
import lombok.Data;

/**
 * Created by Christian on 18-08-2017.
 */
@Data
@Embedded
public class EmbeddedLink {
    private String href;
    private String text;
}
