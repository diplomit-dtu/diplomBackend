package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by Christian on 07-06-2017.
 */
@Data
@Embedded
public class AgendaInfo {
    private String courseName;
    private String courseId;
    private String AgendaId;
}
