package data.sqlImpl;

import data.SQLHandler;
import data.dbDTO.DBInfo;
import data.interfaces.DataBaseDAO;
import data.interfaces.PersistenceException;
import util.RandomString;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLDataBaseDAO implements DataBaseDAO {
    //Database constants
    private final static String
            ADMIN_DBNAME = "useradmin",
            USER_TABLE = "databasestatus",
            USEDB = "USE " + ADMIN_DBNAME + ";";

    //createNewUserDatabase
    private final static String
            START_TRANS     = "START TRANSACTION;",
            CREATE_DB       = "CREATE DATABASE ",
            CREATE_USER     = "CREATE USER ?",
            GRANT_START     = "GRANT ALL PRIVILEGES ON ",
            GRANT_END       = ".* TO ?@'%';",
            SET_PASS        = "SET PASSWORD FOR ?@'%' = PASSWORD(?);",
            USE_DB          = "USE ",
            INSERT_INTO_START = "INSERT INTO ",
            INSERT_INTO_END = " (id, revoked, pass) VALUES (?, ?,?);",
            COMMIT          = "COMMIT;";

    @Override
    public DBInfo createNewUserDatabase(String userID) {
        String pass = new RandomString().nextString();
        String sqlString = START_TRANS +
                CREATE_DB + userID + ";" +
                CREATE_USER + ";" +
                GRANT_START + userID + GRANT_END +
                SET_PASS +
                USE_DB + ADMIN_DBNAME + ";" +
                INSERT_INTO_START + USER_TABLE + INSERT_INTO_END +
                COMMIT;
        try {
            PreparedStatement statement =
                    SQLHandler.getStatement(sqlString);
            statement.setString(1,userID); //create user
            statement.setString(2,userID); //grant table
            statement.setString(3,userID); //grant user
            statement.setString(4,pass); //grant pass
            statement.setString(5,userID); //useradmin id
            statement.setInt(6,0);//useradmin revoked
            statement.setString(7,pass);
            System.out.println(statement);
            boolean execute = statement.execute();
            System.out.println(execute);
            DBInfo dbinfo = DBInfo.builder().id(userID).revoked(false).pass(pass).build();
            return dbinfo;
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public final String

            GETDBINFOS = "SELECT id, revoked, pass FROM databasestatus";
    @Override
    public List<DBInfo> getDBInfos() {
        List<DBInfo> infos = new ArrayList<>();
        try {
            selectUserAdminDB();
            PreparedStatement statement =
                    SQLHandler.getStatement(GETDBINFOS+ ";");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                boolean revoked = resultSet.getBoolean("revoked");
                String pass = resultSet.getString("pass");
                DBInfo dbInfo= DBInfo.builder().id(id).revoked(revoked).pass(pass).build();
                infos.add( dbInfo);
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    public DBInfo getDbInfo(String userID) {
        try {
            selectUserAdminDB();
            PreparedStatement statement =
                    SQLHandler.getStatement(GETDBINFOS + " Where id = ?");
            statement.setString(1,userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                DBInfo dbInfo = getDbInfo(resultSet);
                return dbInfo;
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private DBInfo getDbInfo(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        boolean revoked = resultSet.getBoolean("revoked");
        String pass = resultSet.getString("pass");
        return DBInfo.builder().id(id).revoked(revoked).pass(pass).build();
    }

    private void selectUserAdminDB() throws PersistenceException, SQLException {
        PreparedStatement selectDB = SQLHandler.getStatement(USEDB);
        selectDB.execute();
    }

    private final String
            DROP_DB = "DROP DATABASE IF EXISTS ",
            DROP_USER = "DROP USER ?",
            DELETE_USERADMIN = "DELETE FROM " + USER_TABLE + " WHERE id = ?;";
    @Override
    public void deleteUserDB(String userID) {
        try {
            //DROP DB
            PreparedStatement dropDBStatement = SQLHandler.getStatement(DROP_DB + userID);
            dropDBStatement.execute();
            //DROP USER
            PreparedStatement dropUserStatement = SQLHandler.getStatement(DROP_USER);
            dropUserStatement.setString(1,userID);
            dropUserStatement.execute();
            //DELETE USERDATA
            PreparedStatement deleteUserStatus = SQLHandler.getStatement(DELETE_USERADMIN);
            deleteUserStatus.setString(1,userID);
            deleteUserStatus.execute();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private final String UPDATEPASS = "UPDATE databasestatus SET pass = ? where id = ? ;";
    @Override
    public DBInfo resetPassword(String userID) {
        try {
            selectUserAdminDB();
            String newPass = new RandomString().nextString();
            PreparedStatement statement = SQLHandler.getStatement(UPDATEPASS);
            statement.setString(1,newPass);
            statement.setString(2,userID);
            statement.execute();
            return getDbInfo(userID);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // getDBSizes
    private static final String
            GET_DB_USAGE = "SELECT table_schema 'DBName'," +
            " ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) 'DBSize'" +
            " FROM information_schema.tables" +
            " GROUP BY table_schema;";
    @Override
    public Map<String, Double> getDBSizes() {

        Map<String, Double> sizeMap = new HashMap<>();
        try {
            PreparedStatement statement = SQLHandler.getStatement(GET_DB_USAGE);
            ResultSet execute = statement.executeQuery();
            while(execute.next()){
                String dbName = execute.getString("DBName" );
                Double size = execute.getDouble("DBSize" );
                sizeMap.put(dbName,size);
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sizeMap;
    }

    private static final String
            GET_DB_USER_USAGE = "SELECT table_schema 'DBName'," +
            " ROUND(SUM(data_length + index_length) / 1024 / 1024, 3) 'DBSize'" +
            " FROM information_schema.tables" +
            " Where table_schema = ?;";

    @Override
    public Double getDBSize(String userID) {
        try {
            selectUserAdminDB();
            PreparedStatement statement = SQLHandler.getStatement(GET_DB_USER_USAGE);
            statement.setString(1,userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble("DBSize");
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final String REVOKE_START = "REVOKE ALL PRIVILEGES ON ",
            REVOKE_END = ".* FROM ?@'%';",
            GRANT_SELECT_START  = "GRANT SELECT ON ",
            GRANT_SELECT_END =".* TO ?@'%';",
            UPDATE_REVOKE = "UPDATE " + USER_TABLE + " SET revoked = true where id = ?";

    @Override
    public void revoke(String userID) {
        try {
            selectUserAdminDB();
            PreparedStatement revokeStatement = SQLHandler.getStatement(REVOKE_START + userID + REVOKE_END);
            revokeStatement.setString(1,userID);
            revokeStatement.execute();
            PreparedStatement grantSelectStatement = SQLHandler.getStatement(GRANT_SELECT_START + userID + GRANT_SELECT_END);
            grantSelectStatement.setString(1,userID);
            grantSelectStatement.execute();
            PreparedStatement updateUserAdminData = SQLHandler.getStatement(UPDATE_REVOKE);
            updateUserAdminData.setString(1,userID);
            updateUserAdminData.execute();

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private final String
            GRANTALL_START = "GRANT ALL PRIVILEGES ON ",
            GRANTALL_END = ".* TO ?@'%';",
            UPDATE_GRANT = "UPDATE " + USER_TABLE + " SET revoked = false where id = ?";

    @Override
    public void grant(String userID) {
        try {
            selectUserAdminDB();
            PreparedStatement grantAllStatement = SQLHandler.getStatement(GRANTALL_START + userID + GRANTALL_END);
            grantAllStatement.setString(1,userID);
            grantAllStatement.execute();
            PreparedStatement userAdminUpdate = SQLHandler.getStatement(UPDATE_GRANT);
            userAdminUpdate.setString(1,userID);
            userAdminUpdate.execute();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MySQLDataBaseDAO mySQLDao = new MySQLDataBaseDAO();
        Map<String, Double> dbSizes = mySQLDao.getDBSizes();
        for (Map.Entry entry: dbSizes.entrySet()) {
            System.out.println("" +entry.getKey() + " size:" + entry.getValue());
        }
        List<DBInfo> infos = mySQLDao.getDBInfos();
        for (DBInfo info: infos){
            System.out.println(info);
        }
        String testUser = "s134000";
        mySQLDao.createNewUserDatabase(testUser);
        mySQLDao.resetPassword(testUser);
        DBInfo s134000 = mySQLDao.getDbInfo(testUser);
        System.out.println(s134000);
        System.out.println(mySQLDao.getDBSize(testUser));
        mySQLDao.revoke(testUser);
        System.out.println( mySQLDao.getDbInfo(testUser));
        mySQLDao.grant(testUser);
        System.out.println(mySQLDao.getDbInfo(testUser));
        mySQLDao.deleteUserDB(testUser);
        System.out.println(mySQLDao.getDbInfo(testUser));

    }
}
