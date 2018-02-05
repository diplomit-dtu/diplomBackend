package business.impl;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.CourseActivity;
import data.dbDTO.CourseActivityElement;
import data.dbDTO.CourseActivitySubElement;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleActivityElementParser extends GoogleSheetParser {

    public static CourseActivityElement parseSheet(Spreadsheet sheet) {
        CourseActivityElement activityElement = new CourseActivityElement();
        activityElement.setActivityElementType(CourseActivityElement.ActivityElementType.GoogleSheet);

        Sheet sheet1 = sheet.getSheets().get(0); //get first Sheet
        List<RowData> rowDatas = sheet1.getData().get(0).getRowData(); //Get rows
        boolean headerFound = false;
        if (rowDatas==null) {
            CourseActivitySubElement activitySubElement = new CourseActivitySubElement();
            activitySubElement.setSubElementType(CourseActivitySubElement.SubElementType.Text);
            activitySubElement.setTitle("Google Sheet'et er tomt");
            activityElement.getSubElements().add(activitySubElement);
            return activityElement;
        }
        for (RowData rowData : rowDatas) {
            if (rowData.getValues() != null && rowData.getValues().get(0)!=null && rowData.getValues().get(0).getFormattedValue()!=null) {
                if (!headerFound) {
                    headerFound = true;
                } else {
                    activityElement.getSubElements().add(parseRow(rowData.getValues()));
                }
            } //Else skip row

        }

        return activityElement;
    }

    private static CourseActivitySubElement parseRow(List<CellData> values) {
        CourseActivitySubElement subElement = new CourseActivitySubElement();
        String Content = "";
        int index = 0;
        for (CellData cData : values) {
            if (cData != null && cData.getFormattedValue()!=null) {
                String formattedValue = cData.getFormattedValue();
                switch (index) {
                    case 0: //Title column
                        subElement.setTitle(formattedValue);
                        break;
                    case 1: //Content Column
                        parseContentField(subElement, cData, formattedValue);
                        break;
                    case 2: //Type Column - Intelligent guess on type:
                        parseTypeField(subElement, formattedValue);
                        break;
                    default:
                        //Do nothing about extra Columns...
                        break;
                }
            }
            index++;
        }
        if(CourseActivitySubElement.SubElementType.Embedded_Link.equals(subElement.getSubElementType()) ||
            CourseActivitySubElement.SubElementType.Pop_Out_Link.equals(subElement.getSubElementType())){
            if (!checkLink(subElement)){
                subElement.setSubElementType(CourseActivitySubElement.SubElementType.Text);
                subElement.setContent("Linket er ikke tilgængeligt aktuelt - hvis dette ikke er tilsigtet - så kontakt administrator");
            }
        }
        return subElement;
    }

    private static boolean checkLink(CourseActivitySubElement subElement) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = subElement.getHyperLink();
        boolean linkOk = false;
        if (url!=null) {
            Request request = new Request.Builder().url(url).build();
            try {
                Response execute = okHttpClient.newCall(request).execute();
                if (execute.code() >= 200 && execute.code() <= 204){
                    linkOk = true;
                }
            } catch (IOException e) {
                linkOk=false;
            }

        }
        return linkOk;

    }

    private static void parseTypeField(CourseActivitySubElement subElement, String formattedValue) {
        String typeString = formattedValue.toLowerCase();
        if (subElement.getHyperLink() == null) { //Didn't Find a hyperLink Assume code assignment or free text.
            if (typeString.contains("code")) {
                subElement.setSubElementType(CourseActivitySubElement.SubElementType.Code);
                try {
                    ObjectId oid = new ObjectId(subElement.getContent());
//                    subElement.setId(subElement.getContent()); //Assume that Content is an Id for Native
                } catch (IllegalArgumentException e){
                    //Not a native code element....
                }

            } else {
                subElement.setSubElementType(CourseActivitySubElement.SubElementType.Text);
            }
        } else if (typeString.contains("link")) { //of type link
            if (typeString.contains("embedded")) { //try to embed Contents - better not have too much styling ...
                subElement.setSubElementType(CourseActivitySubElement.SubElementType.Embedded_Link);
            } else { //Show as link
                subElement.setSubElementType(CourseActivitySubElement.SubElementType.Pop_Out_Link);
            }
        } else if (typeString.contains("code")) { //Reference to Code assignment
            subElement.setSubElementType(CourseActivitySubElement.SubElementType.Code);
        } else if (typeString.contains("quiz")) { //
            subElement.setSubElementType(CourseActivitySubElement.SubElementType.Quiz);
        } else if (typeString.contains("concept")){
            subElement.setSubElementType(CourseActivitySubElement.SubElementType.Concept_Question);
        } else { //Assume link is external
            subElement.setSubElementType(CourseActivitySubElement.SubElementType.Pop_Out_Link);
        }
    }

    private static void parseContentField(CourseActivitySubElement subElement, CellData cData, String formattedValue) {
        subElement.setContent(formattedValue);
        String hyperlink = cData.getHyperlink();
        if (hyperlink != null) {
            try {//Look for a string that resembles a google Sheet Id

                String googleId = parseLinkForSheetId(hyperlink);
                subElement.setGoogleSheetId(googleId);
            } catch (IdNotFoundException e) {
                subElement.setGoogleUniqueId(cData.getHyperlink());
            }
            subElement.setHyperLink(hyperlink);
        } else {
            subElement.setGoogleUniqueId(cData.getFormattedValue());
        }
    }
}
