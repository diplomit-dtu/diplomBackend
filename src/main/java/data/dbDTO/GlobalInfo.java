package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class GlobalInfo extends BaseDTO{
    String defaultLinkCollectionId;
    List<String> courseIds;


}
