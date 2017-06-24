package auth;

import data.dbDTO.BaseDTO;
import data.dbDTO.User;
import data.interfaces.BaseDAO;
import data.interfaces.PersistenceException;
import rest.ValidException;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by Christian on 24-06-2017.
 */
public class UserUtil {
    public static User getUserFromContext(ContainerRequestContext requestContext){
        Object property = requestContext.getProperty(AuthorizationFilter.USER);
        if (property instanceof User){
            return (User) property;
        } else {
            return null;
        }
    }
    public static <T extends BaseDAO> void checkAdmin(T baseDAO, String id, ContainerRequestContext requestContext) throws ValidException, PersistenceException, AccessDeniedException {
        User user = UserUtil.getUserFromContext(requestContext);
        BaseDTO baseDTO = baseDAO.get(id);
        if (!baseDTO.getAdmins().contains(user.getId())){
            throw new AccessDeniedException("User does not have permissions to alter resource: + " + id);
        }
    }

}
