package business.interfaces;

import data.dbDTO.StudentAgenda;
import data.interfaces.PersistenceException;
import rest.ElementNotFoundException;
import rest.ValidException;

/**
 * Created by Christian on 25-05-2017.
 */
public interface AgendaController {
    StudentAgenda getAgenda(String courseId, String studentId) throws ValidException, PersistenceException, ElementNotFoundException;
}
