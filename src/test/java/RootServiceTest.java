

import org.junit.Test;
import rest.RootService;

import static org.junit.Assert.*;

/** Test Unit Test
 * Created by Christian on 02-05-2017.
 */
public class RootServiceTest {

    @Test
    public void testGetRoot(){
       assertTrue(new RootService().getRoot().length()>0);
    }
}
