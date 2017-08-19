package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

/**
 * Created by Christian on 18-08-2017.
 */
@Data
@Entity
@Embedded
public class EmbeddedLink {
    private String href;
    private String text;
}
