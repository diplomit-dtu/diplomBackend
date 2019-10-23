package data;

import data.interfaces.PersistenceException;

import java.sql.*;

public class SQLHandler {
    private static Connection conn;

    static {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            refreshConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL-Handler: Not able to establish connection!:" + e.getMessage());
        }
    }

    private static void refreshConnection() throws SQLException {
        if (conn == null || !conn.isValid(100)){
                conn = DriverManager.getConnection("jdbc:" + System.getenv("DIPLOMSQLURL"));
        }
    }

    public static PreparedStatement getStatement(String sql) throws PersistenceException {

        try {
            refreshConnection();
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
