package data.mongoImpl;

import data.dbDTO.StudentAgenda;
import data.interfaces.AgendaDAO;

/**
 * Created by Christian on 07-06-2017.
 */
public class MongoAgendaDAO extends MongoBaseDAO<StudentAgenda> implements AgendaDAO {
    public MongoAgendaDAO() {
        super(StudentAgenda.class);
    }
}
