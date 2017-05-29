package business.interfaces;

import data.dbDTO.StudentAgenda;

/**
 * Created by Christian on 25-05-2017.
 */
public interface AgendaController {
    StudentAgenda getAgenda(String courseId, String studentId);
}
