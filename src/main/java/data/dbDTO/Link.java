package data.dbDTO;

import lombok.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/** Basic LinkDTO
 * Created by Christian on 04-05-2017.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Link {
    @Id
    private ObjectId id;
    @NonNull private String text;
    @NonNull private String href;
    private List<String> addData;

}
