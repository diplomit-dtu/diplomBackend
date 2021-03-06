package business.interfaces;

import data.dbDTO.DBInfo;
import data.interfaces.PersistenceException;

import java.sql.SQLException;
import java.util.List;

public interface UserDataBaseController {
    List<DBInfo> getAllDatabases() throws PersistenceException;

    DBInfo getDatabaseInfo(String userName) throws PersistenceException;

    DBInfo createDB(String userName) throws PersistenceException;

    DBInfo resetPass(String userName) throws PersistenceException;

    void deleteDB(String userName) throws PersistenceException;
}
