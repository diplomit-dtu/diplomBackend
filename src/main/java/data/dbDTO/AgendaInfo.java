package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by Christian on 07-06-2017.
 */
@Data
@Embedded
@NoArgsConstructor
public class AgendaInfo {
    //Loaded via Morphia
    private transient String courseName;
    private String AgendaId;
}
