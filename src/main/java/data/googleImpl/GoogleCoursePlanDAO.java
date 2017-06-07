package data.googleImpl;

import business.impl.GoogleCoursePlanParser;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.CoursePlan;
import data.interfaces.CoursePlanDAO;
import data.interfaces.GoogleSheetsDAO;
import data.interfaces.PersistenceException;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 25-05-2017.
 */
public class GoogleCoursePlanDAO implements CoursePlanDAO{
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
    public CoursePlan get(String oid) throws PersistenceException, ValidException {
        GoogleSheetsDAOImpl googleSheetsDAO = new GoogleSheetsDAOImpl();
        Spreadsheet sheet = googleSheetsDAO.getSheet(oid);
        CoursePlan coursePlan = GoogleCoursePlanParser.parseCoursePlanFromSheet(sheet);
        coursePlan.setGoogleSheetId(oid);
        return coursePlan;
    }

    @Override
    public List<CoursePlan> findByField(String fieldName, String value) throws PersistenceException {
        throw new PersistenceException("Not implemented for Google Courseplan");

    }

    @Override
    public List<CoursePlan> getAll() throws PersistenceException {
        throw new PersistenceException("Only GET implemented for Google API");
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        throw new PersistenceException("Only GET implemented for Google API");
    }
}
