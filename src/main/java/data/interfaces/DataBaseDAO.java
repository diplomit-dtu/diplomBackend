package data.interfaces;



import data.dbDTO.DBInfo;

import java.util.List;
import java.util.Map;

public interface DataBaseDAO {
    DBInfo createNewUserDatabase(String userID);
    List<DBInfo> getDBInfos();
    DBInfo getDbInfo(String userID);
    void deleteUserDB(String userID);
    DBInfo resetPassword(String userID);


    Map<String, Double> getDBSizes();
    Double getDBSize(String userID);
    void revoke(String id);
    void grant(String id);

}
