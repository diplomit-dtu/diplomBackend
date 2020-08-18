package data.dbDTO;

import dev.morphia.annotations.Embedded;
import lombok.Data;

/**
 * Created by Christian on 21-08-2017.
 */
@Data
@Embedded
public class CourseInfoLine {
    String title = "";
    String content = "";
}
