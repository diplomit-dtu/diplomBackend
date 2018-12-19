package rest;

import auth.NotLoggedInException;
import auth.Permission;
import auth.SecureEndpoint;
import config.DeployConfig;
import data.dbDTO.DBInfo;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import util.UserUtil;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

import static business.ControllerRegistry.getUserDatabaseController;

@Path("database")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DatabaseService {
    @Context
    private ContainerRequestContext context;
    private User user;



    @GET
    @SecureEndpoint({Permission.PORTAL_ADMIN,Permission.ADMIN_ALLUSERS})
    public List<DBInfo> getAllDatabases() throws PersistenceException, NotLoggedInException {
        resolveUser();
        return getUserDatabaseController().getAllDatabases();

    }

    @GET
    @Path("{id}")
    @SecureEndpoint({Permission.PORTAL_ADMIN,Permission.ADMIN_ALLUSERS})
    public DBInfo getDatabase(@PathParam("id") String id) throws PersistenceException, NotLoggedInException {
        resolveUser();
        DBInfo databaseInfo = getUserDatabaseController().getDatabaseInfo(id);
        String[] sqlurl = DeployConfig.SQLURL.split("@");
        if (sqlurl.length >= 1) {
            databaseInfo.setHostUrl(sqlurl[sqlurl.length - 1]);
        }
        return databaseInfo;
    }

    @POST
    @Path("{id}/pass")
    @SecureEndpoint({Permission.PORTAL_ADMIN,Permission.ADMIN_ALLUSERS})
    public DBInfo resetPass(@PathParam("id") String id) throws PersistenceException, NotLoggedInException {
        resolveUser();
        return getUserDatabaseController().resetPass(id);
    }

    @DELETE
    @Path("{id}")
    @SecureEndpoint({Permission.PORTAL_ADMIN,Permission.ADMIN_ALLUSERS})
    public void deleteDB(@PathParam("id") String id) throws PersistenceException, NotLoggedInException {
        resolveUser();
        getUserDatabaseController().deleteDB(id);
    }


    public User resolveUser() throws NotLoggedInException {
        user = UserUtil.getUserFromContext(context);
        if (user == null){
            throw new NotLoggedInException("Du er ikke logget ind");
        }
        return user;
    }

    //----SELF----

    @GET
    @Path("self")
    public DBInfo getOwnInfo() throws PersistenceException, NotLoggedInException {
        resolveUser();
        DBInfo databaseInfo = getUserDatabaseController().getDatabaseInfo(user.getUserName());
        String[] sqlurl = DeployConfig.SQLURL.split("@");
        if (databaseInfo!=null && sqlurl.length >= 1) {
            databaseInfo.setHostUrl(sqlurl[sqlurl.length - 1]);
        }
        return databaseInfo;
    }

    @POST
    @Path("self/pass")
    public DBInfo resetPass() throws PersistenceException, NotLoggedInException {
        resolveUser();
        return getUserDatabaseController().resetPass(user.getUserName());
    }

    @POST
    @Path("self")
    public DBInfo createOwnDB() throws SQLException, PersistenceException, NotLoggedInException {
        resolveUser();
        return getUserDatabaseController().createDB(user.getUserName());
    }

    @DELETE
    @Path("self")
    public void deleteDB() throws SQLException, PersistenceException, NotLoggedInException {
        resolveUser();
        getUserDatabaseController().deleteDB(user.getUserName());
    }


}
