package data.mongoImpl;

import data.dbDTO.CourseActivityElement;
import data.interfaces.CourseActivityElementDAO;

/**
 * Created by Christian on 07-08-2017.
 */
public class MongoCourseActivityElementDAO extends MongoBaseDAO<CourseActivityElement> implements CourseActivityElementDAO {
    public MongoCourseActivityElementDAO() {
        super(CourseActivityElement.class);
    }
}
