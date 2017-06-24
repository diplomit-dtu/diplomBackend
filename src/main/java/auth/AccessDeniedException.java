package auth;

/**
 * Created by Christian on 24-06-2017.
 */
public class AccessDeniedException extends Throwable {
    public AccessDeniedException(String s) {
        super(s);
    }
}
