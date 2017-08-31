package business.interfaces;

import data.dbDTO.CoursePlan;
import data.interfaces.PersistenceException;

/**
 * Created by Christian on 06-08-2017.
 */
public interface CoursePlanController {

    void syncCoursePlan(String googleSheetPlanId, String coursePlanId) throws ValidException, PersistenceException;

    CoursePlan createCoursePlan() throws PersistenceException;
}
