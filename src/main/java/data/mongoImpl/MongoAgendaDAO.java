package data.mongoImpl;

import data.dbDTO.Agenda;
import data.interfaces.AgendaDAO;

/**
 * Created by Christian on 07-06-2017.
 */
public class MongoAgendaDAO extends MongoBaseDAO<Agenda> implements AgendaDAO {
    public MongoAgendaDAO() {
        super(Agenda.class);
    }
}
