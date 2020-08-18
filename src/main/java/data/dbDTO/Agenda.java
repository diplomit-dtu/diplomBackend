package data.dbDTO;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Agenda extends BaseDTO {

    @Embedded
    private Map<String, ElementMetaData> elementMetaData = new HashMap<>(); //<ElementID>, "list of SubElements
    private List<Link> courseLinks;


}
