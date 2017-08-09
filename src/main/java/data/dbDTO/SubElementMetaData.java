package data.dbDTO;



import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongodb.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.List;

/** MetaData for ActivitySubElements
 * Created by Christian on 07-06-2017.
 */
@EqualsAndHashCode()
@Data
@Embedded
public class SubElementMetaData{

    private String googleUniqueId; //used to track progress and notes in Google CoursePlan based
    private String notes;
    private Double progression;
    transient private Boolean checked;
    private List<String> solutions = new ArrayList<>();

    public Boolean getChecked(){
        return (progression!=null && progression >0.9);
    }
}
