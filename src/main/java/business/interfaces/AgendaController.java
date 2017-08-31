package business.interfaces;

import data.dbDTO.Agenda;
import data.dbDTO.AgendaInfo;
import data.interfaces.PersistenceException;
import data.interfaces.ElementNotFoundException;

/**
 * Created by Christian on 25-05-2017.
 */
public interface AgendaController {
    Agenda getAgenda(String agendaId) throws ValidException, PersistenceException, ElementNotFoundException;
    Agenda saveAgenda(Agenda agenda) throws PersistenceException;

    AgendaInfo createNewAgenda(AgendaInfo info, String userFromContext) throws ValidException, PersistenceException;

    Boolean deleteAgenda(String agendaId) throws ValidException, PersistenceException;
}
