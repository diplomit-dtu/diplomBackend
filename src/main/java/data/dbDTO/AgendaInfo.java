package data.dbDTO;

import dev.morphia.annotations.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Created by Christian on 07-06-2017.
 */
@Data
@Embedded
@NoArgsConstructor
public class AgendaInfo {
    //Loaded via Morphia
    private transient String courseName;
    private transient boolean admin=false;
    private String AgendaId;
}
