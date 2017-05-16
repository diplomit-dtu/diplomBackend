package rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new WebApplicationException("Something went wrong while filtering request");
        } else {
            HttpServletRequest httpRequest = ((HttpServletRequest) request);
            HttpServletResponse httpResponse = ((HttpServletResponse) response);

            String url = httpRequest.getRequestURI();
            String params = httpRequest.getQueryString();
            String authHeader = httpRequest.getHeader("Authorization");
            System.out.println("Caught in filter - Url: " + url + " Query: " + params + "Headers: " + authHeader);
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
            httpResponse.addHeader("Access-Control-Allow-Credentials:", "true");
            httpResponse.addHeader("Access-Control-Expose-Headers","Authorization");
            chain.doFilter(request,response);
        }
    }

    public void destroy() {

    }
}
