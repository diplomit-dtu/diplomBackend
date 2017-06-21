package auth;

/**
 * Created by Christian on 21-06-2017.
 */
public class MalformedAuthorizationHeaderException extends Exception {
    public MalformedAuthorizationHeaderException(String authorization) {
        super(authorization);
    }
}
