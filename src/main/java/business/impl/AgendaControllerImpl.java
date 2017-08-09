package business.impl;

import business.interfaces.AgendaController;
import business.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.AgendaDAO;
import business.interfaces.CourseController;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoAgendaDAO;
import rest.ElementNotFoundException;
import rest.ValidException;

/** AgendaController - handles StudentAgendas
 * Created by Christian on 25-05-2017.
 */
public class AgendaControllerImpl implements AgendaController {
    private AgendaDAO agendaDAO = new MongoAgendaDAO();

    @Override
    public Agenda getAgenda(String agendaId) throws ValidException, PersistenceException, ElementNotFoundException {
        Agenda agenda = agendaDAO.get(agendaId);
        if (agenda == null) throw new ElementNotFoundException("Agenda not found!");

        return agenda;
    }

    @Override
    public Agenda saveAgenda(Agenda agenda) throws PersistenceException {
        return agendaDAO.save(agenda);
    }


    @Override
    public AgendaInfo createNewAgenda(AgendaInfo info, String userId) throws ValidException, PersistenceException {
        Agenda agenda = new Agenda();
        agenda.getAdmins().add(userId);
        Agenda savedAgenda = agendaDAO.save(agenda);
        info.setAgendaId(savedAgenda.getId());
        return info;
    }

    @Override
    public Boolean deleteAgenda(String agendaId) throws ValidException, PersistenceException {
        return agendaDAO.delete(agendaId);
    }

}
