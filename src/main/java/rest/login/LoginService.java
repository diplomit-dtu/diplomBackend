package rest.login;

import auth.JWTHandler;
import business.ControllerRegistry;
import business.interfaces.UserController;
import config.DeployConfig;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.ElementNotFoundException;
import util.FileLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** backdoor login
 * Created by Christian on 29-04-2017.
 */
@Path("guestlogin")
public class LoginService {

    @GET
    public Response test() throws IOException, PersistenceException {
        UserController userController= ControllerRegistry.getUserController();
        User user = null;
        try {
            user = userController.getUserByCampusNetId("guest");
        } catch (ElementNotFoundException e) {
            user = new User();
            user.setUserName("guest");
            userController.saveUser(user);
        }
        String jwtToken = new JWTHandler().generateJwtToken(user);
        //Generating redirection page and returning it.
        String html = generateRedirectPage(jwtToken);
        return Response.ok().entity(html)
                .header("Authorization", "Bearer " + jwtToken)
                .build();


    }


    private String generateRedirectPage(String jwtToken) throws IOException {
        Map<String,Object> content = new HashMap<>();
        content.put("redirectName", DeployConfig.PORTAL_NAME);
        content.put("redirectUrl",DeployConfig.PORTAL_FRONT_URL + "?token=" + jwtToken);
        return FileLoader.loadMustache("/redirect.mustache",content);

    }

}
