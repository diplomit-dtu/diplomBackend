package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/** LinkCollection
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class LinkCollection extends BaseDTO {
    private String course;
    @Reference
    private List<Link> links = new ArrayList<>();
    @Reference
    private User owner;


}
