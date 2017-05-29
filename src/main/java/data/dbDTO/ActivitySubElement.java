package data.dbDTO;

import lombok.Data;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
public class ActivitySubElement extends BaseDTO{

    @NonNull
    private String title;

    private Map<String,String> ContentBlocks = new LinkedHashMap<>();
}
