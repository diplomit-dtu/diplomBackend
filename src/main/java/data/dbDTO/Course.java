package data.dbDTO;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import java.time.LocalDate;

/**
 * Created by Christian on 11-05-2017.
 */
@Data
@Entity
@RequiredArgsConstructor
public class Course {
    public static final String COURSE_ID = "courseId";

    @Id
    private ObjectId objectId;
    @NonNull @Property(COURSE_ID)
    private String courseId;
    @NonNull
    private String courseName;
    @NonNull
    private LocalDate startingDate;
    @NonNull
    private LocalDate endingDate;

    //Referenced documents
    private ObjectId linkCollection;

    //Reference for fetching courseplan
    private ObjectId coursePlan;

    //TODO add relevant ekstra information

}
