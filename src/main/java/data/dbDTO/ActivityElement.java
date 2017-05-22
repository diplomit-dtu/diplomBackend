package data.dbDTO;

import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@NoArgsConstructor
public class ActivityElement extends BaseDTO {
    private String title;
    @Reference
    private List<ActivitySubElement> subElements = new ArrayList<>();
}
