package data.mongoImpl;

import data.MorphiaHandler;
import data.dbDTO.Course;
import data.interfaces.CourseDAO;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.List;

/**
 * Created by Christian on 15-05-2017.
 */
public class MongoCourseDAO extends MongoBaseDAO<Course> implements CourseDAO {
    public MongoCourseDAO() {
        super(Course.class);
    }

    @Override
    public Course findCourseByCourseID(String courseId) throws PersistenceException {
        Datastore dataStore = MorphiaHandler.getInstance().getDatastore();
        List<Course> courses = dataStore.createQuery(Course.class)
                .field("course").equal(courseId).asList();
        if (courses.size()>1) throw new PersistenceException("more than one course with this ID exists! - Contact your sysadmin");
        if (courses.size()<1) return null;
        return courses.get(0);
    }

}
