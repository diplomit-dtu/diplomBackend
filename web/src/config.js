

export default class Config{

    //Use local development server when running in development mode!
    static ApiPath = (process.env.NODE_ENV ==="development") ? "http://localhost:8080" : "";
    // static ApiPath = "https://diplomportal.herokuapp.com"
    static campusNetServiceUrl = "/rest/cn/login";
    static TOKEN_NAME  = "portal-jwt-Token";
    // ApiPath for same site installation
    //  static ApiPath = "";
}