import auth.AuthorizationFilter;
import auth.Permission;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.dbDTO.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Christian on 23-06-2017.
 */
public class AuthorizationFilterTest {
    User user;


    @Before
    public void setup(){
        user = new User();
        user.getPermissions().add(Permission.ADMIN_ALLUSERS);
        Role role = new Role();//Add nested permission
        role.getPermissions().add(Permission.OWN_ID);
        user.getRoles().add(role);
        Role role2 = new Role();
        user.getRoles().add(role2);
    }

    @Test
    public void testFilterPositive() {
        AuthorizationFilter filter = new AuthorizationFilter();
       // for (int i = 0; i < 1000; i++) {
            assertTrue(filter.checkPermissions(user, Permission.ADMIN_ALLUSERS));
            assertTrue(filter.checkPermissions(user, Permission.OWN_ID));
            assertTrue(filter.checkPermissions(user, Permission.OWN_ID, Permission.ADMIN_ALLUSERS));
            assertTrue(filter.checkPermissions(user, Permission.ADMIN_ALLUSERS,Permission.GET_COURSE));
            assertTrue(filter.checkPermissions(user, Permission.OWN_ID,Permission.GET_COURSE));
       // }
    }

    @Test
    public void testFilterNegative(){
        AuthorizationFilter filter = new AuthorizationFilter();
        assertFalse(filter.checkPermissions(user,Permission.USER_UPDATE_SELF));
        assertFalse(filter.checkPermissions(user, Permission.USER_UPDATE_SELF,Permission.GET_COURSE));
    }
}
