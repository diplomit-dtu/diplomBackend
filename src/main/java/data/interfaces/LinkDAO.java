package data.interfaces;

import data.dbDTO.Link;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public interface LinkDAO extends BaseDAO<Link> {
}
