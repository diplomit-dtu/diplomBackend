package dao;

import data.dbDTO.CoursePlan;
import data.googleImpl.GoogleCoursePlanDAO;
import data.interfaces.PersistenceException;
import org.junit.Test;
import business.interfaces.ValidException;

/** Test Unit Test
 * Created by Christian on 02-05-2017.
 */
public class GoogleCoursePlanDAOTest {
    private final String testSheetId = "1Zj-1eLX67PQRzM7m1icq2vSXzbHn2iFvN4V9cUHTWQo";

    @Test
    public void testGetCoursePlane() throws ValidException, PersistenceException {
        GoogleCoursePlanDAO googleCoursePlanDAO = new GoogleCoursePlanDAO();
        CoursePlan coursePlan = googleCoursePlanDAO.get(testSheetId);
        System.out.println(coursePlan);


    }
}
