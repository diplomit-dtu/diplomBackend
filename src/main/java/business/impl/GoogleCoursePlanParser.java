package business.impl;

import com.google.api.services.sheets.v4.model.*;
import data.dbDTO.CourseActivityElement;
import data.dbDTO.CourseActivity;
import data.dbDTO.CoursePlan;

import java.util.List;

/**
 * Created by Christian on 25-05-2017.
 */
public class GoogleCoursePlanParser extends GoogleSheetParser {

    public static CoursePlan parseCoursePlanFromSheet(Spreadsheet spreadsheet) {
        CoursePlan coursePlan = new CoursePlan();
        coursePlan.setGoogleSheetId(spreadsheet.getSpreadsheetId());
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rowData = gridData.getRowData();
        boolean headerFound = false;
        int rowNo = 0;
        for (RowData row : rowData) {
            //Check if row contains anything interesting...
            if (row.getValues() != null && row.getValues().get(0)!=null && row.getValues().get(0).getFormattedValue()!=null) { //discard empty rows
                if (!headerFound) { // look for a header row
                    headerFound = true;
                    parseHeader(coursePlan, row.getValues());
                } else {
                    coursePlan.getCourseActivityList().add(parseActivityRow(row));
                }
            } else {
                //else skip rows
                if (headerFound) break; // If there is empty rows after  header line - stop parsing
            }
            rowNo++;
        }
        return coursePlan;
    }

    private static void parseHeader(CoursePlan coursePlan, List<CellData> cellDatas) {
        for (CellData cellData: cellDatas){
            String formattedValue = cellData.getFormattedValue();
            if ((formattedValue != null && formattedValue != "")) {
                coursePlan.getHeaders().add(formattedValue);
            }
        }
    }

    private static CourseActivity parseActivityRow(RowData row) {
        List<CellData> cellDatas = row.getValues();
        int index = 0;
        CourseActivity activity = new CourseActivity();
        for (CellData cellD : cellDatas) {
            String formattedValue = cellD.getFormattedValue();
            if (formattedValue == null){
                //Do nothing
            } else if (index == 0) { //First row is title
                if (formattedValue.toLowerCase().contains("kladde")) {
                    activity.setStatus(CourseActivity.ActivityStatus.DRAFT);
                } else {
                    activity.setStatus(CourseActivity.ActivityStatus.VISIBLE);
                }
                activity.setTitle(formattedValue);
            } else if (index == 1) { //Second Row - description
                activity.setDescription(formattedValue);
            } else if (index == 2 && cellD.getEffectiveValue() != null && cellD.getEffectiveFormat().getNumberFormat() != null) {
                // Third Row - date
                Double numberValue = cellD.getEffectiveValue().getNumberValue();
                activity.setEndDate(convertGoogleDate(numberValue));
            } else { //Following rows are elements in the activity
                CourseActivityElement activityElement = new CourseActivityElement();
                String link = cellD.getHyperlink();
                if (link != null) { //Look for link
                    try {
                        //Check to see if link contains a Google Sheet Id
                        String googleSheetId = parseLinkForSheetId(link);
                        activityElement.setGoogleSheetId(googleSheetId);
                        activityElement.setActivityElementType(CourseActivityElement.ActivityElementType.GoogleSheet);
                        activityElement.setHyperLink(link);
                        activityElement.setGoogleSheetId(googleSheetId);
                    } catch (IdNotFoundException e) {
                        activityElement.setActivityElementType(CourseActivityElement.ActivityElementType.Link);
                        activityElement.setGoogleUniqueId(link);
                    }
                    activityElement.setHyperLink(link);
                } else {
                    activityElement.setActivityElementType(CourseActivityElement.ActivityElementType.Text);
                    activityElement.setGoogleUniqueId(cellD.getFormattedValue());
                }

                activityElement.setTitle(cellD.getFormattedValue());

                activity.getActivityElementList().add(activityElement);
            }
            index++;
        }
        return activity;
    }

}
