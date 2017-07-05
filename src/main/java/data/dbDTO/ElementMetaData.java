package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 05-07-2017.
 */
@Data
public class ElementMetaData extends BaseDTO {
    @Embedded
    private List<SubElementMetaData> metaDataList = new ArrayList<>();
    private Double progress;

}
