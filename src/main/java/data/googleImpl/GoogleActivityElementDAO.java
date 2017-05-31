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

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleActivityElementDAO implements ActivityElementDAO  {
    GoogleSheetsDAO googleSheetsDAO = new GoogleSheetsDAOImpl();

    @Override
    public ActivityElement save(ActivityElement element) throws PersistenceException {
        throw new PersistenceException("Only GET implemented for GoogleActivityElementDAO");
    }

    @Override
    public List<ActivityElement> saveMultiple(List<ActivityElement> elements) throws PersistenceException {
        throw new PersistenceException("Only GET implemented for GoogleActivityElementDAO");
    }

    @Override
    public ActivityElement get(String oid) throws PersistenceException, ValidException {
        Spreadsheet sheet = googleSheetsDAO.getSheet(oid);
        ActivityElement activityElement = GoogleActivityElementParser.parseSheet(sheet);
        return activityElement;

    }

    @Override
    public List<ActivityElement> getAll() throws PersistenceException {
        throw new PersistenceException("Only GET implemented for GoogleActivityElementDAO");
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        throw new PersistenceException("Only GET implemented for GoogleActivityElementDAO");
    }
}
