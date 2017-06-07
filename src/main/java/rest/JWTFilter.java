package rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;

import static config.Config.DEBUG;


/**
 * Created by Christian on 04-05-2017.
 */
public class JWTFilter implements Filter {


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
        if(DEBUG)  System.out.println("Caught in filter - Url: " + url + " Query: " + params + ", AuthHeaders: " + authHeader + ", Method: " + request.getMethod());
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
