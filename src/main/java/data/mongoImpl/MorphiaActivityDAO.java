package data.mongoImpl;

import data.MorphiaHandler;
import data.dbDTO.CourseActivity;
import data.interfaces.PersistenceException;
import dev.morphia.dao.BasicDAO;
import dev.morphia.dao.DAO;

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
