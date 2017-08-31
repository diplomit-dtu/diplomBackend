package business.impl;

import business.interfaces.ActivityElementController;
import data.dbDTO.CourseActivityElement;
import data.googleImpl.GoogleActivityElementDAO;
import data.interfaces.ActivityElementDAO;
import data.interfaces.PersistenceException;
import business.interfaces.ValidException;

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleActivityElementController implements ActivityElementController {
    ActivityElementDAO activityElementDAO = new GoogleActivityElementDAO();
    @Override
    public CourseActivityElement getGoogleActivityElement(String googleSheetId) throws ValidException, PersistenceException {
        CourseActivityElement courseActivityElement = activityElementDAO.get(googleSheetId);
        courseActivityElement.setGoogleUniqueId(googleSheetId);
        return courseActivityElement;
    }
}
