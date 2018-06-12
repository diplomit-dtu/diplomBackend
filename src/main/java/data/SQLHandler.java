package data;

import data.dbDTO.DBInfo;
import data.interfaces.PersistenceException;

import java.sql.*;

public class SQLHandler {
    private static Connection conn;
    private static SQLHandler sqlHandler;

    static {
        refreshConnection();
    }

    private static void refreshConnection() {
        if (sqlHandler == null){
            sqlHandler = new SQLHandler();
        }
        if (conn == null){
            try {
                conn = DriverManager.getConnection("jdbc:mysql://diplomportal.c2nouactg6m6.eu-west-1.rds.amazonaws.com?" +
                        "user=root&password=Splat1477");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static SQLHandler getInstance(){
        refreshConnection();
        return sqlHandler;
    }

    public PreparedStatement getStatement(String sql) throws PersistenceException {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    public static void main(String[] args) throws PersistenceException, SQLException {
        PreparedStatement s134000 = SQLHandler.getInstance().getStatement("USE useradmin");
        boolean execute = s134000.execute();
        PreparedStatement statement = SQLHandler.getInstance().getStatement("SELECT * from databasestatus");
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            boolean revoked = resultSet.getBoolean("revoked");
            String id = resultSet.getString("id");
            String pass = resultSet.getString("pass");
            DBInfo build = DBInfo.builder().id(id).pass(pass).revoked(revoked).build();
            System.out.println(build);
        }


    }
}
