package rest;

import auth.JWTHandler;
import auth.Permission;
import auth.SecureEndpoint;
import data.dbDTO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import sun.nio.cs.ext.SimpleEUCEncoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static config.Config.DEBUG;


/**
 * Created by Christian on 04-05-2017.
 */
@Provider
@SecureEndpoint
public class JWTFilter implements Filter {
    @Context
    private ResourceInfo resourceInfo;


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new WebApplicationException("Something went wrong while filtering request");
        } else {
            HttpServletRequest httpRequest = ((HttpServletRequest) request);
            HttpServletResponse httpResponse = ((HttpServletResponse) response);
            //Set CORS headers so API can be used
            setCorsHeaders(httpRequest, httpResponse);
            //Check permissions
            permissionFilter((HttpServletRequest) request, httpRequest);
            chain.doFilter(request,response);
        }
    }

    private void permissionFilter(HttpServletRequest request, HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURI();
        String params = httpRequest.getQueryString();
        String authHeader = httpRequest.getHeader("Authorization");
        //TODO replace with log4j
        if(DEBUG)  System.out.println("Caught in filter - Url: " + url + " Query: " + params + ", AuthHeaders: " + authHeader + ", Method: " + request.getMethod());
        if(DEBUG) System.out.println("Caught in filter - Authentication: " + authHeader);
        String[] splitAuthHeader = new String[1];
        if(authHeader!=null) {
            splitAuthHeader = authHeader.split(" ");
        }
        if (splitAuthHeader.length==2){
            try {
                Jws<Claims> claimsJws = JWTHandler.validateToken(splitAuthHeader[1]);
                User user = claimsJws.getBody().get("user",User.class);
                if (DEBUG) System.out.println("User found: " + user);
                checkPermissions(user);
            } catch (JWTHandler.AuthException e) {
                throw new WebApplicationException(e.getMessage(),403);
            }
        } else {
            throw new WebApplicationException("You need to login", 401);
        }

    }

    private void checkPermissions(User user) {
        SecureEndpoint classAnnotation = resourceInfo.getResourceClass().getAnnotation(SecureEndpoint.class);
        Permission[] value = classAnnotation.value();
        for (Permission p: value
             ) {
            System.out.println(p);
        }
        resourceInfo.getResourceMethod().getAnnotation(SecureEndpoint.class);
    }

    private void setCorsHeaders(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, PATCH");
        String requestAllowHeader = httpRequest.getHeader("Access-Control-Request-Headers");
        httpResponse.setHeader("Access-Control-Allow-Headers",requestAllowHeader);
        httpResponse.setHeader("Access-Control-Allow-Credentials:", "true");
        httpResponse.setHeader("Access-Control-Expose-Headers","Authorization");
    }

    public void destroy() {

    }
}
