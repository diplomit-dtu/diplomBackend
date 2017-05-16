package rest;

import config.Config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/** Initial Configuration
 * Created by Christian on 28-04-2017.
 */
@ApplicationPath(Config.BASE_API_PATH)
public class AppConfig extends Application{


}
