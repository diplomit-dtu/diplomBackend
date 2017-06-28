package config;

/**
 * Created by Christian on 28-04-2017.
 */
public class DeployConfig {
    //----Constants----
    private static final String DEFAULT_CN_REDIRECT_URL = "http://localhost:8080" + Config.BASE_API_PATH + "/" + Config.CN_SERVICE_PATH;
    public static final String PORTAL_NAME = "DiplomPortal";

    //----Environment Variables - Urls:
    public static final String CN_REDIRECT_URL = (System.getenv("CAMPUSNET_REDIRECT_URL") != null) ?
            System.getenv("CAMPUSNET_REDIRECT_URL"): DEFAULT_CN_REDIRECT_URL;
    public static final String MONGODB_URI = System.getenv("MONGODB_URI");
    public static final String DATABASE_URL = System.getenv("DATABASE_URL");

    public static final String JWT_SECRET_KEY = System.getenv("JWT_SECRET_KEY");
    public static final String PORTAL_FRONT_URL = (System.getenv("PORTAL_FRONT_URL") != null) ?
            System.getenv("PORTAL_FRONT_URL"):"/";
    public static final String GOOGLE_API_KEY = System.getenv("GOOGLE_API_KEY");

    public static final Boolean NON_PROD = (System.getenv("NON_PROD") !=null);



    public static void main(String[] args) {
        System.out.println(CN_REDIRECT_URL);
        System.out.println(MONGODB_URI);
        System.out.println(DATABASE_URL);
        System.out.println(JWT_SECRET_KEY);
    }
}
