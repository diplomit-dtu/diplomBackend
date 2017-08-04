package data.googleImpl;

import business.impl.GoogleCoursePlanParser;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.CoursePlan;
import data.interfaces.CoursePlanDAO;
import data.interfaces.GoogleSheetsDAO;
import data.interfaces.PersistenceException;
import rest.ValidException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 25-05-2017.
 */
public class GoogleCoursePlanDAO implements CoursePlanDAO{
    protected static final String ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API = "Only GET implemented for Google API";
    GoogleSheetsDAO gDAO = new GoogleSheetsDAOImpl();
    @Override
    public CoursePlan save(CoursePlan element) throws PersistenceException {
        throw new PersistenceException("Only GET implemented for Google API");
    }

    @Override
    public List<CoursePlan> saveMultiple(List<CoursePlan> elements) throws PersistenceException {
        throw new PersistenceException("Only GET implemented for Google API");
    }

    @Override
    public CoursePlan get(String id) throws PersistenceException, ValidException {
        GoogleSheetsDAOImpl googleSheetsDAO = new GoogleSheetsDAOImpl();
        Spreadsheet sheet = googleSheetsDAO.getSheet(id);
        CoursePlan coursePlan = GoogleCoursePlanParser.parseCoursePlanFromSheet(sheet);
        coursePlan.setGoogleSheetId(id);
        return coursePlan;
    }

    @Override
    public List<CoursePlan> multiGet(Collection<String> ids) throws PersistenceException {
        throw new PersistenceException((ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API));
    }

    @Override
    public List<CoursePlan> findByField(String fieldName, String value) throws PersistenceException {
        throw new PersistenceException("Not implemented for Google Courseplan");

    }

    @Override
    public List<CoursePlan> findByFields(Map<String, Object> fields) throws PersistenceException {
        throw new PersistenceException(ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API);
    }

    @Override
    public int findByFieldAndUpdateField(String findField, Object findFieldValue, String updateField, Object newValue) throws PersistenceException {
        throw new PersistenceException(ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API);
    }

    @Override
    public List<CoursePlan> getAll() throws PersistenceException {
        throw new PersistenceException(ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API);
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        throw new PersistenceException(ONLY_GET_IMPLEMENTED_FOR_GOOGLE_API);
    }
}
