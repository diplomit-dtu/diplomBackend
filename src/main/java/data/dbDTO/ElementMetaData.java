package data.dbDTO;

import dev.morphia.annotations.Embedded;
import lombok.Data;

import java.util.HashMap;
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
