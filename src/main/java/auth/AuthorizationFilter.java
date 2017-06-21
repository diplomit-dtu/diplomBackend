package auth;

import business.impl.UserControllerImpl;
import business.interfaces.UserController;
import data.dbDTO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

import static config.Config.DEBUG;

/**
 * Created by Christian on 21-06-2017.
 */
@Provider
@SecureEndpoint
public class AuthorizationFilter implements ContainerRequestFilter {
    @Context
    ResourceInfo resourceInfo;
    @Context
    HttpHeaders headers;
    private UserController userController = new UserControllerImpl();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Caught in authFilter!");
        SecureEndpoint annotation = resourceInfo.getResourceMethod().getAnnotation(SecureEndpoint.class);
        if (annotation==null){ //No annotation on method level
            annotation = resourceInfo.getResourceClass().getAnnotation(SecureEndpoint.class);
        }
        if (annotation==null) { //No annotation on class level
            return; //No need to verify - EndPoint is not secured!
        }
        try {
            User user = resolveUser();
        } catch (NotLoggedInException| JWTHandler.ExpiredLoginException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
        } catch (MalformedAuthorizationHeaderException|JWTHandler.AuthException e) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
        }
        Permission[] value = annotation.value();
        for (Permission permission: value) {
            System.out.println(permission);
        }
    }

    private User resolveUser() throws NotLoggedInException, MalformedAuthorizationHeaderException, JWTHandler.AuthException, JWTHandler.ExpiredLoginException {
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new NotLoggedInException("No securityToken - You need to login!");
        } else {
            String[] splitAuthHeader = authHeader.split(" ");
            if (splitAuthHeader.length != 2) {
                throw new MalformedAuthorizationHeaderException("Authorization header malformed: " + authHeader + ", Should be: 'Authorization: Bearer <JWTTOKEN>'");
            } else {
                    Jws<Claims> claimsJws = JWTHandler.validateToken(splitAuthHeader[1]);
                    User user = claimsJws.getBody().get("user",User.class);
                    if (DEBUG) System.out.println("User found: " + user);
                    return user;

            }

        }
    }


    private void checkPermissions(User user) {
        System.out.println(resourceInfo);
        SecureEndpoint classAnnotation = resourceInfo.getResourceClass().getAnnotation(SecureEndpoint.class);
        Permission[] value = classAnnotation.value();
        for (Permission p: value
             ) {
            System.out.println(p);
        }
        resourceInfo.getResourceMethod().getAnnotation(SecureEndpoint.class);
    }


}
