package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** Root Servlet
 * Created by Christian on 28-04-2017.
 */
@Path("")
public class RootService {
    @GET
    public String getRoot(){
        return "API Description coming up";
    }
}
