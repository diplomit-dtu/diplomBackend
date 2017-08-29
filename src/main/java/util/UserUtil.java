package util;

import auth.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.dbDTO.BaseDTO;
import data.dbDTO.User;
import data.interfaces.BaseDAO;
import data.interfaces.PersistenceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import rest.ValidException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import static config.Config.DEBUG;

/**
 * Created by Christian on 24-06-2017.
 */
public class UserUtil {
    protected static final String DELIMITER = " ";
    public static final String USER = "user";

    public static User getUserFromContext(ContainerRequestContext requestContext){
        Object property = requestContext.getProperty(AuthorizationFilter.USER);
        if (property instanceof User){
            return (User) property;
        } else {
            return null;
        }
    }

    public static <T extends BaseDTO> void checkAdmin(T baseDTO, ContainerRequestContext requestContext) throws AccessDeniedException {
        User userFromContext = getUserFromContext(requestContext);
        if(!baseDTO.getAdmins().contains(userFromContext.getId())){
            throw new AccessDeniedException("User does not have permissions to alter resource: "+ baseDTO.getId());
        }

    }

    public static User resolveUser(HttpHeaders headers) throws NotLoggedInException, MalformedAuthorizationHeaderException, JWTHandler.AuthException, JWTHandler.ExpiredLoginException {
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new NotLoggedInException("No securityToken - You need to login!");
        } else {
            String[] splitAuthHeader = authHeader.split(DELIMITER);
            if (splitAuthHeader.length != 2) {
                throw new MalformedAuthorizationHeaderException("Authorization header malformed: " + authHeader + ", Should be: 'Authorization: Bearer <JWTTOKEN>'");
            } else {
                Jws<Claims> claimsJws = new JWTHandler().validateToken(splitAuthHeader[1]);
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.convertValue(claimsJws.getBody().get("user"),User.class);
                if (DEBUG) System.out.println("User found: " + user);
                return user;
            }

        }
    }

}
