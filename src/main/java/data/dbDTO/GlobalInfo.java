package data.dbDTO;

import dev.morphia.annotations.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Deprecated
public class GlobalInfo extends BaseDTO{
    String defaultLinkCollectionId;
    List<String> courseIds = new ArrayList<>();


}
