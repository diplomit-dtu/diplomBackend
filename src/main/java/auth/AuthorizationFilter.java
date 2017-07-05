package auth;

import business.impl.UserControllerImpl;
import business.interfaces.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.ControllerRegistry;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.dbDTO.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static config.Config.DEBUG;

/** Authorizationfilter - intercepts jwtTokens and validates global permissions. Resourcelevel Permissions are evaluated af resource level.
 * Created by Christian on 21-06-2017.
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthorizationFilter implements ContainerRequestFilter {
    protected static final String DELIMITER = " ";
    public static final String USER = "user";
    @Context
    ResourceInfo resourceInfo;
    @Context
    HttpHeaders headers;
    private UserController userController = new UserControllerImpl();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Caught in authFilter!");
        SecureEndpoint annotation = resourceInfo.getResourceMethod().getAnnotation(SecureEndpoint.class);
        if (annotation == null) { //No annotation on method level
            annotation = resourceInfo.getResourceClass().getAnnotation(SecureEndpoint.class);
        }
        if (annotation == null) { //No annotation on class level
            setUserOnContext(requestContext);
            return; //No need to verify - EndPoint is not secured!
        }
        User user = null;
        try {
            user = resolveUser();
            System.out.println("User validated: " + user);
            Permission[] permissions = annotation.value();
            if (!checkPermissions(user, permissions)){
                String allowedPerms ="";
                for (Permission perm : permissions) {
                    allowedPerms+= " " + perm;
                }
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("You do not have the right privilige: " + allowedPerms).build());
            }
            requestContext.setProperty(USER, user);
            //Phew everything checked out! request ok!
        } catch (NotLoggedInException | JWTHandler.ExpiredLoginException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
        } catch (MalformedAuthorizationHeaderException | JWTHandler.AuthException e) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
        }

    }

    private void setUserOnContext(ContainerRequestContext requestContext) {
        try {
            User user = resolveUser();
            requestContext.setProperty(USER,user);
        } catch (NotLoggedInException| JWTHandler.AuthException e) {
            //No problem - proceed as anonymous
        } catch (MalformedAuthorizationHeaderException e) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
        } catch (JWTHandler.ExpiredLoginException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
        }

    }

    public boolean checkPermissions(User user, Permission... permissions) {
        for (Permission permission : permissions) {

            if (user != null && user.getPermissions() != null && user.getPermissions().contains(permission)) {
                return true; //Users own permissions contains permission
            } else {
                if (user != null && user.getRoles() != null && checkRoles(permission, user.getRoles())) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean checkRoles(Permission permission, List<Role> roles) {
        for (Role role :roles) {
            if (role.getPermissions().contains(permission)){
                return true;
            }
            if(checkRoles(permission, role.getInheritsPermissionsFromRoles())){
                return true;
            }
        }
        return false;

    }

    private User resolveUser() throws NotLoggedInException, MalformedAuthorizationHeaderException, JWTHandler.AuthException, JWTHandler.ExpiredLoginException {
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
