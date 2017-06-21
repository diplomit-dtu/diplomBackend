package auth;

/**
 * Created by Christian on 21-06-2017.
 */
public class NotLoggedInException extends Exception {
    public NotLoggedInException(String s) {
        super(s);
    }
}
