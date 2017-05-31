package business.interfaces;

import data.dbDTO.ActivityElement;
import data.interfaces.PersistenceException;
import rest.ValidException;

/**
 * Created by Christian on 31-05-2017.
 */
public interface ActivityElementController {
    ActivityElement getGoogleActivityElement(String googleSheetId) throws ValidException, PersistenceException;
}
