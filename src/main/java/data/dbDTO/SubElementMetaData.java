package data.dbDTO;



import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/** MetaData for ActivitySubElements
 * Created by Christian on 07-06-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubElementMetaData extends BaseDTO{
    private String notes;
    private Double progression;
    private List<String> solutions = new ArrayList<>();

}
