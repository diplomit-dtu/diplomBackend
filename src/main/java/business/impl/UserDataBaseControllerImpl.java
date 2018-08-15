package business.impl;

import business.interfaces.UserDataBaseController;
import data.dbDTO.DBInfo;
import data.interfaces.DataBaseDAO;
import data.interfaces.PersistenceException;
import data.sqlImpl.MySQLDataBaseDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDataBaseControllerImpl implements UserDataBaseController {
    DataBaseDAO dataBaseDAO = new MySQLDataBaseDAO();

    @Override
    public List<DBInfo> getAllDatabases() throws PersistenceException {
        try {
            Map<String, Double> dbSizes = dataBaseDAO.getDBSizes();
            List<DBInfo> dbInfos = dataBaseDAO.getDBInfos();
            for (DBInfo db: dbInfos) {
              db.setSize(dbSizes.get(db.getId()));
            }
            return dbInfos;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public DBInfo getDatabaseInfo(String userName) throws PersistenceException {
        try {
            Double dbSize = dataBaseDAO.getDBSize(userName);
            DBInfo dbInfo = dataBaseDAO.getDbInfo(userName);
            if (dbInfo==null) {return dbInfo;}
            dbInfo.setSize(dbSize);
            return dbInfo;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public DBInfo createDB(String userName) throws  PersistenceException {
        DBInfo newUserDatabase = null;
        try {
            newUserDatabase = dataBaseDAO.createNewUserDatabase(userName);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
        Double dbSize = dataBaseDAO.getDBSize(userName);
        newUserDatabase.setSize(dbSize);
        return newUserDatabase;
    }

    @Override
    public DBInfo resetPass(String userName) throws  PersistenceException {
        DBInfo dbInfo = null;
        try {
            dbInfo = dataBaseDAO.resetPassword(userName);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
        Double dbSize = dataBaseDAO.getDBSize(userName);
        dbInfo.setSize(dbSize);
        return dbInfo;
    }

    @Override
    public void deleteDB(String userName) throws  PersistenceException {
        try {
            dataBaseDAO.deleteUserDB(userName);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}
