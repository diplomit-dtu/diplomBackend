import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** Service for debugging online
 * Created by Christian on 28-04-2017.
 */
@Path("debug")
public class DebugService {

    @Path("env")
    @GET
    public String getEnv(){
        return System.getenv().toString();

    }
}
