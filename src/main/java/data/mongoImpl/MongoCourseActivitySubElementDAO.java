package data.mongoImpl;

import data.dbDTO.CourseActivitySubElement;
import data.interfaces.CourseActivitySubElementDAO;

/**
 * Created by Christian on 07-08-2017.
 */
public class MongoCourseActivitySubElementDAO extends MongoBaseDAO<CourseActivitySubElement>implements CourseActivitySubElementDAO {
    public MongoCourseActivitySubElementDAO() {
        super(CourseActivitySubElement.class);
    }
}
