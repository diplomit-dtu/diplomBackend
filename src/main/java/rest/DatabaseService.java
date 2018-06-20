package rest;

import auth.NotLoggedInException;
import auth.Permission;
import auth.SecureEndpoint;
import static business.ControllerRegistry.*;

import data.dbDTO.DBInfo;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import util.UserUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.util.List;

@Path("database")
public class DatabaseService {
    @Context
    private ContainerRequestContext context;

    @GET
    @SecureEndpoint({Permission.PORTAL_ADMIN,Permission.ADMIN_ALLUSERS})
    public List<DBInfo> getAllDatabases() throws PersistenceException {
        return getUserDatabaseController().getAllDatabases();

    }

    @GET
    @Path("self")
    public DBInfo getOwnInfo() throws PersistenceException, NotLoggedInException {
        User userFromContext = UserUtil.getUserFromContext(context);
        if (userFromContext!=null) {
            return getUserDatabaseController().getDatabaseInfo(userFromContext.getUserName());
        }else {
            throw new NotLoggedInException("Du er ikke logget ind");
        }
    }

}
