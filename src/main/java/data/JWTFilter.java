package data;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;

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
            System.out.println("Caught in filter:" + url);
            chain.doFilter(request,response);
        }
    }

    public void destroy() {

    }
}
