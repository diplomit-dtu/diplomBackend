package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 05-07-2017.
 */
@Data
public class ElementMetaData{
    @Embedded
    private Map<String, SubElementMetaData> metaDataList = new HashMap<>();
    private Double progress;
}
