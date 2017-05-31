package business.impl;

import business.interfaces.ActivityElementController;
import data.dbDTO.ActivityElement;
import data.googleImpl.GoogleActivityElementDAO;
import data.interfaces.ActivityElementDAO;
import data.interfaces.PersistenceException;
import rest.ValidException;

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleActivityElementController implements ActivityElementController {
    ActivityElementDAO activityElementDAO = new GoogleActivityElementDAO();
    @Override
    public ActivityElement getGoogleActivityElement(String googleSheetId) throws ValidException, PersistenceException {
        return activityElementDAO.get(googleSheetId);
    }
}
