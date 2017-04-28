package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** Testing Service
 * Created by Christian on 28-04-2017.
 */
@Path("hello")
public class HelloService {
    @GET
    public String getHello(){
        return "HELLO From Jersey!";
    }
}
