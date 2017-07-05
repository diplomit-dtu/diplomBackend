package data.dbDTO;

import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christian on 05-07-2017.
 */
@Data
public class GoogleCoursePlanMetaDataTree {
    private String coursePlanId;

    @Embedded
    private Map<String, ElementMetaData> metaDataMap = new HashMap<>();

}
