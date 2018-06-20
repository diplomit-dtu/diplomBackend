package data;

import data.interfaces.PersistenceException;

import java.sql.*;

public class SQLHandler {
    private static Connection conn;

    static {
        try {
            refreshConnection();
        } catch (SQLException e) {
            System.err.println("SQL-Handler: Not able to establish connection!");
        }
    }

    private static void refreshConnection() throws SQLException {
        if (conn == null || !conn.isValid(100)){
                conn = DriverManager.getConnection("jdbc:mysql://diplomportal.c2nouactg6m6.eu-west-1.rds.amazonaws.com?" +
                        "user=root&password=" + System.getenv("diplomportalsqlpass"));
        }
    }

    public static PreparedStatement getStatement(String sql) throws PersistenceException, SQLException {
        refreshConnection();
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    public static void main(String[] args) throws PersistenceException, SQLException {
        PreparedStatement s134000 = SQLHandler.getStatement("USE useradmin");
        boolean execute = s134000.execute();
        PreparedStatement statement = SQLHandler.getStatement("SELECT * from databasestatus");
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            boolean revoked = resultSet.getBoolean("revoked");
            String id = resultSet.getString("id");
            String pass = resultSet.getString("pass");
        }


    }
}
