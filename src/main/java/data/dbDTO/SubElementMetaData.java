package data.dbDTO;



import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/** MetaData for ActivitySubElements
 * Created by Christian on 07-06-2017.
 */
@Data
class SubElementMetaData {
    private String notes;
    private Double progression;
    private List<String> solutions = new ArrayList<>();

}
