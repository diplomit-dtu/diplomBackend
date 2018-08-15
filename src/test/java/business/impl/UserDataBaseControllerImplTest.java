package business.impl;

import business.interfaces.UserDataBaseController;
import data.SQLHandler;
import data.dbDTO.DBInfo;
import data.interfaces.PersistenceException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDataBaseControllerImplTest {
    UserDataBaseController db = new UserDataBaseControllerImpl();
    public final String testUser = "JUnitTestUser";

    @Test
    public void createDB() throws SQLException, PersistenceException {
        DBInfo newdb = this.db.createDB(testUser);
        System.out.println(newdb);
    }
    @Test
    public void getAllDatabases() throws PersistenceException {
        System.out.println(db.getAllDatabases());
    }

    @Test
    public void getDatabaseInfo() throws PersistenceException {
        System.out.println(db.getDatabaseInfo(testUser));
    }


    @Test
    public void resetPass() throws SQLException, PersistenceException {
        DBInfo dbInfo = db.resetPass(testUser);
        System.out.println(dbInfo);
        System.out.println(db.getDatabaseInfo(testUser));
    }

    @Test
    public void z_deleteDB() throws SQLException, PersistenceException {
        db.deleteDB(testUser);
        System.out.println(db.getDatabaseInfo(testUser));
    }
}