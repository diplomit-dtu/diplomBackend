package data.googleImpl;

import business.impl.GoogleActivityElementParser;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.CourseActivityElement;
import data.interfaces.ActivityElementDAO;
import data.interfaces.GoogleSheetsDAO;
import data.interfaces.PersistenceException;
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
    public CourseActivityElement save(CourseActivityElement element) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<CourseActivityElement> saveMultiple(List<CourseActivityElement> elements) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public CourseActivityElement get(String id) throws PersistenceException, ValidException {
        Spreadsheet sheet = googleSheetsDAO.getSheet(id);
        return GoogleActivityElementParser.parseSheet(sheet);

    }

    @Override
    public List<CourseActivityElement> findByField(String fieldName, String value) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<CourseActivityElement> findByFields(Map<String, Object> fields) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public int findByFieldAndUpdateField(String findField, Object findFieldValue, String updateField, Object newValue) throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public List<CourseActivityElement> getAll() throws PersistenceException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        throw new PersistenceException(NOT_IMPLEMENTED_FOR_GOOGLE_ACTIVITY_ELEMENT_DAO);
    }
}
