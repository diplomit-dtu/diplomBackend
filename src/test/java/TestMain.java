import org.bson.types.ObjectId;

/**
 * Created by Christian on 18-05-2017.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(new ObjectId().toHexString());
    }
}
