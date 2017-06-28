package rest;

import auth.JWTHandler;
import business.impl.UserControllerImpl;
import business.interfaces.UserController;
import data.dbDTO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.annotation.Priority;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static config.Config.DEBUG;


/** Filter that allows CORS - disable for non-CORS
 * Created by Christian on 04-05-2017.
 */
@Priority(500) //Before AuthorizationFilter (1000) to make sure that headers always get injected
@Provider
public class CORSFilter implements ContainerRequestFilter {
     @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    private UserController userController = new UserControllerImpl();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Caught in CORSFilter");
        setCORSHeaders();
    }


    private void setCORSHeaders() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, PATCH");
        String requestAllowHeader = request.getHeader("Access-Control-Request-Headers");
        response.setHeader("Access-Control-Allow-Headers",requestAllowHeader);
        response.setHeader("Access-Control-Allow-Credentials:", "true");
        response.setHeader("Access-Control-Expose-Headers","Authorization");
    }

}
