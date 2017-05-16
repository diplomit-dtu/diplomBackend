package data.mongoImpl;

import data.dbDTO.CoursePlan;
import data.interfaces.CoursePlanDAO;

/**
 * Created by Christian on 15-05-2017.
 */
public class MongoCoursePlanDAO extends MongoBaseDAO<CoursePlan> implements CoursePlanDAO {
    public MongoCoursePlanDAO() {
        super(CoursePlan.class);
    }
}
