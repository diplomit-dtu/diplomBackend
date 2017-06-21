package data.googleImpl;

import business.impl.GoogleActivityElementParser;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.ActivityElement;
import data.interfaces.ActivityElementDAO;
import data.interfaces.GoogleSheetsDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoBaseDAO;
import rest.ValidException;

import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleActivityElementDAO implements ActivityElementDAO  {
    private static final String NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO = "Not implemented for GoogleActivityElementDAO";
    private GoogleSheetsDAO googleSheetsDAO = new GoogleSheetsDAOImpl();

    @Override
    public ActivityElement save(ActivityElement element) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<ActivityElement> saveMultiple(List<ActivityElement> elements) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public ActivityElement get(String oid) throws PersistenceException, ValidException {
        Spreadsheet sheet = googleSheetsDAO.getSheet(oid);
        return GoogleActivityElementParser.parseSheet(sheet);

    }

    @Override
    public List<ActivityElement> findByField(String fieldName, String value) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<ActivityElement> findByFields(Map<String, Object> fields) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<ActivityElement> getAll() throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }
}
