package data.dbDTO;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@Embedded
@RequiredArgsConstructor
public class ActivityElement {
    @NonNull
    String title;

    List<ActivitySubElement> subElements = new ArrayList<>();
}
