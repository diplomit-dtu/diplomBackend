package data.mongoImpl;

import data.dbDTO.CourseActivity;
import data.interfaces.CourseActivityDAO;

/**
 * Created by Christian on 07-08-2017.
 */
public class MongoCourseActivityDAO extends MongoBaseDAO<CourseActivity> implements CourseActivityDAO  {
    public MongoCourseActivityDAO() {
        super(CourseActivity.class);
    }
}
