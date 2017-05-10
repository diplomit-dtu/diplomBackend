package rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by Christian on 04-05-2017.
 */
public class JWTFilter implements Filter {


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new WebApplicationException("Something went wrong while filtering request");
        } else {
            HttpServletRequest httpRequest = ((HttpServletRequest) request);
            String url = httpRequest.getRequestURI();
            String params = httpRequest.getQueryString();
            String authHeader = httpRequest.getHeader("Authorization");
            System.out.println("Caught in filter - Url: " + url + " Query: " + params + "Headers: " + authHeader);

            chain.doFilter(request,response);
        }
    }

    public void destroy() {

    }
}
