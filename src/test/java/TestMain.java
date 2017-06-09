import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/** Testing class for
 * Created by Christian on 18-05-2017.
 */
public class TestMain {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        String spreadsheetId = "1Zj-1eLX67PQRzM7m1icq2vSXzbHn2iFvN4V9cUHTWQo"; // TODO: Update placeholder value.

        // The ranges to retrieve from the spreadsheet.
        List<String> ranges = new ArrayList<>(); // TODO: Update placeholder value.
//        ranges.add("Ark1!A1:C3");
        // True if grid data should be returned.
        // This parameter is ignored if a field mask was set in the request.
        boolean includeGridData = true; // TODO: Update placeholder value.

        Sheets sheetsService = createSheetsService();
        Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(spreadsheetId);

        request.setRanges(ranges);
        System.out.println(DeployConfig.GOOGLE_API_KEY);
        request.setKey(DeployConfig.GOOGLE_API_KEY);
        request.setIncludeGridData(includeGridData);

        Spreadsheet response = request.execute();
        Sheet sheet = response.getSheets().get(0);

        // TODO: Change code below to process the `response` object:
        System.out.println(response);
        for (RowData data: sheet.getData().get(0).getRowData()){
            System.out.println(data.getValues());
        }
        CellData cellData = sheet.getData().get(0).getRowData().get(3).getValues().get(3);
        System.out.println(cellData.getFormattedValue());
        System.out.println(cellData.getEffectiveValue());
        System.out.println(cellData.getUserEnteredValue());
        System.out.println(cellData.getEffectiveFormat());
        System.out.println(cellData.getHyperlink());


//        Sheets.Spreadsheets.Values.Get valuesGet = sheetsService.spreadsheets().values().get(spreadsheetId,"");
//        ValueRange execute = valuesGet.execute();
//        System.out.println(execute);
    }

    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // TODO: Change placeholder below to generate authentication credentials. See
        // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
        //
        // Authorize using one of the following scopes:
        //   "https://www.googleapis.com/auth/drive"
        //   "https://www.googleapis.com/auth/drive.readonly"
        //   "https://www.googleapis.com/auth/spreadsheets"
        //   "https://www.googleapis.com/auth/spreadsheets.readonly"
        GoogleCredential credential = null;

        return new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google-SheetsSample/0.1")
                .build();
    }
}

