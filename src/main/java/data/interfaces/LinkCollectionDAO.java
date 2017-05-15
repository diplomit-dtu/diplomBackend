package data.interfaces;

import data.dbDTO.LinkCollection;

/**
 * Created by Christian on 11-05-2017.
 */
public interface LinkCollectionDAO extends BaseDAO<LinkCollection>{
    LinkCollection getByCourse(String Course);
}
