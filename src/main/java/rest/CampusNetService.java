package rest;

import config.Config;
import deployment.DeployConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

/** Service for Campusnet redirects
 * Created by Christian on 28-04-2017.
 */
@Path("cn")
public class CampusNetService {
    @Path("login")
    @GET
    public Response getLogin(){
        URI uri = URI.create(Config.CAMPUSNET_LOGIN_URL + "?service=" + DeployConfig.CN_REDIRECT_URL);
        return Response.temporaryRedirect(uri).build();
    }

    @GET
    public Response getRedirect(@QueryParam("ticket") String ticket){
        OkHttpClient client = new OkHttpClient();
        String url = Config.CAMPUSNET_VALIDATE_URL + "?service=" + DeployConfig.CN_REDIRECT_URL + "&ticket=" + ticket;
        Request request = new Request.Builder().url(url).build();
        String validationReply = "";
        try {
            okhttp3.Response response = client.newCall(request).execute();
            validationReply = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok().entity("ticket:" + ticket + ", validation: " + validationReply).build();
    }
}
