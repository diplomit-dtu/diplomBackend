package data.interfaces;

import com.google.api.services.sheets.v4.model.Spreadsheet;

/**
 * Created by Christian on 24-05-2017.
 */
public interface GoogleSheetsDAO {
    Spreadsheet getSheet(String sheetId);
}
