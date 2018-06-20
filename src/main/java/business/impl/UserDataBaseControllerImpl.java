package business.impl;

import business.interfaces.UserDataBaseController;
import data.dbDTO.DBInfo;
import data.interfaces.DataBaseDAO;
import data.interfaces.PersistenceException;
import data.sqlImpl.MySQLDataBaseDAO;

import java.sql.SQLException;
import java.util.List;

public class UserDataBaseControllerImpl implements UserDataBaseController {
    DataBaseDAO dataBaseDAO = new MySQLDataBaseDAO();

    @Override
    public List<DBInfo> getAllDatabases() throws PersistenceException {
        try {
            return dataBaseDAO.getDBInfos();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public DBInfo getDatabaseInfo(String userName) throws PersistenceException {
        try {
            return dataBaseDAO.getDbInfo(userName);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }
}
