package data.interfaces;

import data.dbDTO.GlobalInfo;
import business.interfaces.ValidException;

/**
 * Created by Christian on 18-05-2017.
 */
public interface GlobalInfoDAO extends BaseDAO<GlobalInfo>{
    GlobalInfo getInfo() throws ValidException, PersistenceException;
}
