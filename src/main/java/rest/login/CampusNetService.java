package rest.login;

import business.impl.UserControllerImpl;
import business.interfaces.UserController;
import config.Config;
import config.DeployConfig;
import data.JWTHandler;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rest.ElementNotFoundException;
import util.FileLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for Campusnet redirects
 * Created by Christian on 28-04-2017.
 */
@Path(Config.CN_SERVICE_PATH)
public class CampusNetService {
    private UserController userController = new UserControllerImpl();


    //STEP 1: Redirecting to campusNet Authentication
    @Path(Config.CN_SERVICE_LOGIN)
    @GET
    public Response getLogin() {
        URI uri = URI.create(Config.CAMPUSNET_LOGIN_URL + "?service=" + DeployConfig.CN_REDIRECT_URL);
        return Response.temporaryRedirect(uri).build();
    }

    //STEP 2: Getting redirectURL from Campusnet:
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getToken(@QueryParam("ticket") String ticket) {
        // STEP 3: Exchange Ticket for Login Details:
        OkHttpClient client = new OkHttpClient();
        String url = Config.CAMPUSNET_VALIDATE_URL + "?service=" + DeployConfig.CN_REDIRECT_URL + "&ticket=" + ticket;
        Request request = new Request.Builder().url(url).build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            String validationReply = response.body().string();
            String[] validationArray = validationReply.split("\n");
            String frontUrl = (DeployConfig.PORTAL_FRONT_URL == null) ? "" : DeployConfig.PORTAL_FRONT_URL;
            String jwtToken = "";
            //STEP 4: Issue Token and redicret to frontpage including token in url:
            if (validationArray.length == 2 && validationArray[0].toLowerCase().trim().equals("yes")) { //Login success
                User user = resolveUser(validationArray[1]);

                jwtToken = JWTHandler.generateJwtToken(user);
                String html = generateRedirectPage(jwtToken);
                System.out.println(html);
                return Response.ok().entity(html)
                        .header("Authorization", "Bearer " + jwtToken)
                        .build();
            } else {
                return Response.status(401).entity("Login failed, reply was: "  + validationReply + "<br><a href='" + frontUrl +"'>Return to frontPage</a>").build();
            }
        } catch (IOException e) {
            return Response.serverError().entity("Could not connect to campusnet-auth").build();
        }

    }

    private String generateRedirectPage(String jwtToken) throws IOException {
        Map<String,Object> content = new HashMap<>();
        content.put("redirectName",DeployConfig.PORTAL_NAME);
        content.put("redirectUrl",DeployConfig.PORTAL_FRONT_URL + "?token=" + jwtToken);
        return FileLoader.loadMustache("/redirect.mustache",content);

    }

    private User resolveUser(String cnId){
        User userByCampusNetId = null;
        try {
            userByCampusNetId = userController.getUserByCampusNetId(cnId);
            if (userByCampusNetId==null){
                throw new ElementNotFoundException("null User in db");
            }
        } catch (ElementNotFoundException e) {
            userByCampusNetId = new User(cnId);
            try {
                userByCampusNetId = userController.saveUser(userByCampusNetId);
            } catch (PersistenceException e1) {
               userByCampusNetId = userController.getAnonymous();
            }

        } catch (PersistenceException e) {
            userByCampusNetId = userController.getAnonymous();
        }

        return userByCampusNetId;
    }
}
