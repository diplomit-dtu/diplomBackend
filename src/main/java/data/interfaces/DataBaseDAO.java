package data.interfaces;



import data.dbDTO.DBInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataBaseDAO {
    DBInfo createNewUserDatabase(String userID) throws SQLException, PersistenceException;
    List<DBInfo> getDBInfos() throws SQLException, PersistenceException;
    DBInfo getDbInfo(String userID) throws SQLException, PersistenceException;
    void deleteUserDB(String userID) throws SQLException, PersistenceException;
    DBInfo resetPassword(String userID) throws SQLException, PersistenceException;


    Map<String, Double> getDBSizes() throws SQLException, PersistenceException;
    Double getDBSize(String userID);
    void revoke(String id);
    void grant(String id);

}
