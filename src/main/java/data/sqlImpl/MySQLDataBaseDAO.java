package data.sqlImpl;

import data.SQLHandler;
import data.dbDTO.DBInfo;
import data.interfaces.DataBaseDAO;
import data.interfaces.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import util.RandomString;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
@Slf4j
public class MySQLDataBaseDAO implements DataBaseDAO {
//Database constants
    private final static String
            ADMIN_DBNAME = "useradmin",
            USER_TABLE = "databasestatus";

    private final static String
            START_TRANS ="START TRANSACTION;",
            CREATE_DB = "CREATE DATABASE ",
            CREATE_USER = "CREATE USER ",
            GRANT_START = "GRANT ALL PRIVILEGES ON ",
            GRANT_END =        ".* TO ?@'%';",
            SET_PASS = "SET PASSWORD FOR ?@'%' = PASSWORD(?);",
            USE_DB = "USE ",
            INSERT_INTO_START = "INSERT INTO ",
            INSERT_INTO_END = " (id, revoked, status) VALUES (?, ?,?);",
            COMMIT = "COMMIT;";

    @Override
    public DBInfo createNewUserDatabase(String userID) {
        String pass = new RandomString().nextString();
        String sqlString = START_TRANS +
                CREATE_DB + userID + ";" +
                CREATE_USER + userID + ";" +
                GRANT_START + userID + GRANT_END +
                SET_PASS +
                USE_DB + ADMIN_DBNAME + ";" +
                INSERT_INTO_START + USER_TABLE + INSERT_INTO_END +
                COMMIT;
        try {
            PreparedStatement statement =
                    SQLHandler.getStatement(sqlString);
            statement.setString(1,userID); //grant table
            statement.setString(2,userID); //grant user
            statement.setString(3,pass); //grant pass
            statement.setString(4,userID);
            statement.setInt(5,0);
            statement.setString(6,pass);
            System.out.println(statement);
//            boolean execute = statement.execute();
//            System.out.println(execute);

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DBInfo.builder().id(userID).revoked(false).pass(pass).build();
    }

    @Override
    public List<DBInfo> getDBInfos() {
        return null;
    }

    @Override
    public void deleteUserDB(String userID) {

    }

    @Override
    public DBInfo resetPassword(String userID) {
        return null;
    }

    @Override
    public Map<String, Double> getDBSizes() {
        return null;
    }

    @Override
    public void revoke(String id) {

    }

    @Override
    public void grant(String id) {

    }

    public static void main(String[] args) {
        new MySQLDataBaseDAO().createNewUserDatabase("test");
    }
}
