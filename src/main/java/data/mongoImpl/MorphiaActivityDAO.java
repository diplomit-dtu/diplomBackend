package data.mongoImpl;

import com.mongodb.MongoClient;
import data.MorphiaHandler;
import data.dbDTO.CourseActivity;
import data.interfaces.PersistenceException;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.dao.DAO;

/**
 * Created by Christian on 22-05-2017.
 */
public class MorphiaActivityDAO extends BasicDAO<CourseActivity,String> implements DAO<CourseActivity,String> {
    public MorphiaActivityDAO() throws PersistenceException {
        super(CourseActivity.class, MorphiaHandler.getDS());
    }

    public static void main(String[] args) throws PersistenceException {
        MorphiaActivityDAO morphiaActivityDAO = new MorphiaActivityDAO();
        CourseActivity activity = new CourseActivity();
        activity.setDescription("Forberedelse");
        morphiaActivityDAO.save(activity);
    }
}
