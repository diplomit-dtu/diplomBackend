package business.impl;

import com.google.api.services.sheets.v4.model.*;
import data.dbDTO.ActivityElement;
import data.dbDTO.CourseActivity;
import data.dbDTO.CoursePlan;

import java.util.Date;
import java.util.List;

/**
 * Created by Christian on 25-05-2017.
 */
public class GoogleCoursePlanParser {

    protected static final int GOOGLEEPOCHOFFSET = 25569;

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
                    coursePlan.getCourseActivityList().add(parseRow(row));
                }
            } //else skip rows
        }
        return coursePlan;
    }

    private static CourseActivity parseRow(RowData row) {
        List<CellData> cellDatas = row.getValues();
        int index = 0;
        CourseActivity activity = new CourseActivity();
        for (CellData cellD : cellDatas) {
            String formattedValue = cellD.getFormattedValue();
            if (index == 0) { //First row is title
                if (formattedValue.toLowerCase().contains("kladde")){
                    activity.setStatus(CourseActivity.ActivityStatus.DRAFT);
                } else {
                    activity.setStatus(CourseActivity.ActivityStatus.VISIBLE);
                }
                activity.setTitle(formattedValue);
            } else if (index == 1) { //Second Row description
                activity.setDescription(formattedValue);
            } else if (index == 2 && cellD.getEffectiveValue()!=null && cellD.getEffectiveFormat().getNumberFormat()!=null){
                Double numberValue = cellD.getEffectiveValue().getNumberValue();
                long millisSince1970 = getMillisSince1970(numberValue);
                activity.setEndDate(new Date(millisSince1970));
            } else {
                ActivityElement activityElement = new ActivityElement();
                String link = cellD.getHyperlink();
                boolean isGoogleSheet = parseLinkForSheet(link);
                activityElement.setActivityElementType(ActivityElement.ActivityElementType.Link);
                activityElement.setTitle(cellD.getFormattedValue());
                activityElement.setHyperLink(cellD.getHyperlink());
                activity.getActivityElementList().add(activityElement);
            }
            index++;
        }
        return activity;
    }

    private static long getMillisSince1970(Double numberValue) {
        numberValue-= GOOGLEEPOCHOFFSET; //Fix google offset 30/12 1899
        long millisSince1970 = (long) (numberValue * 24*60*60*1000); //Convert from days to millis
        long rest = millisSince1970 %1000; // Round off millis to avoid rounding errors (13:00 -> 12:59)
        millisSince1970 += (rest>500 ?rest : -rest);
        return millisSince1970;
    }

    private static boolean parseLinkForSheet(String link) {
        if (link != null) {
            String[] urlParts = link.split("/");
            if (urlParts.length >= 4 && "spreadsheets".equals(urlParts[3])) {
                return true;
            }
        }
        return false;
    }
}
