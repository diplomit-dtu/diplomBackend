package data.googleImpl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import config.DeployConfig;
import data.googleDTO.GoogleCell;
import data.googleDTO.GoogleRow;
import data.interfaces.GoogleSheetsDAO;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 24-05-2017.
 */
public class GoogleSheetsDAOImpl implements GoogleSheetsDAO {
    private String apiKey = DeployConfig.GOOGLE_API_KEY;

    @Override
    public Spreadsheet getSheet(String sheetId) {
        try {
            Sheets sheetsService = createApiKeySheetService();
            Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(sheetId);
            request.setKey(apiKey);
            request.setIncludeGridData(true);
            Spreadsheet spreadSheet = request.execute();
            return spreadSheet;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GoogleRow> parse(Spreadsheet spreadSheet) {
        Sheet sheet = spreadSheet.getSheets().get(0);
        List<GoogleRow> googleRows = new ArrayList<>();

        for (RowData data : sheet.getData().get(0).getRowData()) {
            GoogleRow googleRow = parseRow(data);
            googleRows.add(googleRow);
        }

        return googleRows;
    }

    public GoogleRow parseRow(RowData data) {
        if (data == null || data.getValues()==null) return null;
        GoogleRow googleRow = new GoogleRow();
        for (CellData cData : data.getValues()) {
            if (cData == null) {
                googleRow.addCell(null);
            } else {
                GoogleCell googleCell = new GoogleCell();
                googleCell.setText(cData.getFormattedValue());
                googleCell.setHref(cData.getHyperlink());
                googleRow.addCell(googleCell);
            }
        }
        return googleRow;
    }

    private Sheets createApiKeySheetService() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Sheets.Builder(httpTransport, jsonFactory, null)
                .setApplicationName("SimplePortal")
                .build();
    }

}
