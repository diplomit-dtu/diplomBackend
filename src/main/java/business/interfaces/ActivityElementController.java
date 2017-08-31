package business.interfaces;

import data.dbDTO.CourseActivityElement;
import data.interfaces.PersistenceException;

/**
 * Created by Christian on 31-05-2017.
 */
public interface ActivityElementController {
    CourseActivityElement getGoogleActivityElement(String googleSheetId) throws ValidException, PersistenceException;
}
