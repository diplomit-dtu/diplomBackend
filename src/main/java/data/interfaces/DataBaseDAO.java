package data.interfaces;



import data.dbDTO.DBInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataBaseDAO {
    DBInfo createNewUserDatabase(String userID) throws PersistenceException, SQLException;
    List<DBInfo> getDBInfos() throws PersistenceException, SQLException;
    DBInfo getDbInfo(String userID) throws PersistenceException, SQLException;
    void deleteUserDB(String userID) throws PersistenceException, SQLException;
    DBInfo resetPassword(String userID) throws PersistenceException, SQLException;


    Map<String, Double> getDBSizes() throws PersistenceException, SQLException;
    Double getDBSize(String userID);
    void revoke(String id);
    void grant(String id);

}
