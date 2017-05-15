package data.dbDTO;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/** LinkCollection
 * Created by Christian on 11-05-2017.
 */
@Entity
@Data
@RequiredArgsConstructor
public class LinkCollection {

    @Id
    private ObjectId id;
    @NonNull
    private String course;
    @Reference
    private List<Link> links = new ArrayList<>();
    @Reference
    private User owner;


}
