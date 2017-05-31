package business.impl;

import com.google.api.services.sheets.v4.model.*;
import data.dbDTO.ActivityElement;
import data.dbDTO.CourseActivity;
import data.dbDTO.CoursePlan;

import java.util.List;

/**
 * Created by Christian on 25-05-2017.
 */
public class GoogleCoursePlanParser extends GoogleSheetParser {

    public static CoursePlan parseCoursePlanFromSheet(Spreadsheet spreadsheet) {
        CoursePlan coursePlan = new CoursePlan();
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rowData = gridData.getRowData();
        boolean headerFound = false;
        for (RowData row : rowData) {
            if (row.getValues() != null) { //discard empty rows
                if (!headerFound) { // look for a header row
                    headerFound = true;
                    //Skip header line
                } else {
                    coursePlan.getCourseActivityList().add(parseActivityRow(row));
                }
            } //else skip rows
        }
        return coursePlan;
    }

    private static CourseActivity parseActivityRow(RowData row) {
        List<CellData> cellDatas = row.getValues();
        int index = 0;
        CourseActivity activity = new CourseActivity();
        for (CellData cellD : cellDatas) {
            String formattedValue = cellD.getFormattedValue();
            if (index == 0) { //First row is title
                if (formattedValue.toLowerCase().contains("kladde")) {
                    activity.setStatus(CourseActivity.ActivityStatus.DRAFT);
                } else {
                    activity.setStatus(CourseActivity.ActivityStatus.VISIBLE);
                }
                activity.setTitle(formattedValue);
            } else if (index == 1) { //Second Row description
                activity.setDescription(formattedValue);
            } else if (index == 2 && cellD.getEffectiveValue() != null && cellD.getEffectiveFormat().getNumberFormat() != null) {
                Double numberValue = cellD.getEffectiveValue().getNumberValue();
                activity.setEndDate(convertGoogleDate(numberValue));
            } else {
                ActivityElement activityElement = new ActivityElement();
                String link = cellD.getHyperlink();
                if (link != null) {
                    try {
                        String googleSheetId = parseLinkForSheetId(link);
                        activityElement.setGoogleSheetId(googleSheetId);
                        activityElement.setActivityElementType(ActivityElement.ActivityElementType.GoogleSheet);
                    } catch (IdNotFoundException e) {
                        activityElement.setActivityElementType(ActivityElement.ActivityElementType.Link);
                    }
                    activityElement.setHyperLink(link);
                } else {
                    activityElement.setActivityElementType(ActivityElement.ActivityElementType.Text);
                }

                activityElement.setTitle(cellD.getFormattedValue());

                activity.getActivityElementList().add(activityElement);
            }
            index++;
        }
        return activity;
    }

}
