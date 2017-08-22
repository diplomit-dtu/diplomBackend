package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by Christian on 21-08-2017.
 */
@Data
@Embedded
public class CourseInfoLine {
    String title = "";
    String content = "";
}
