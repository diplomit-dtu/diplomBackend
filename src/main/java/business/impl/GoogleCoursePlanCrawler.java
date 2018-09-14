package business.impl;

import business.interfaces.CourseController;
import business.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.PersistenceException;
import business.interfaces.ValidException;

import java.util.List;

/**
 * Created by Christian on 05-07-2017.
 */
public class GoogleCoursePlanCrawler {

    public static Agenda crawlCoursePlan(String coursePlanId) throws ValidException, PersistenceException {
        GoogleCoursePlanMetaDataTree googleTree = new GoogleCoursePlanMetaDataTree();
        CourseController courseController = ControllerRegistry.getCourseController();
        CoursePlan googleCoursePlan = courseController.getGoogleCoursePlan(coursePlanId);

        List<CourseActivity> courseActivityList = googleCoursePlan.getCourseActivityList();
        for (CourseActivity activity: courseActivityList
                ) {
            List<CourseActivityElement> activityElementList = activity.getActivityElementList();
            crawl(googleTree, activityElementList);
        }
        System.out.println("---");
        System.out.println(googleTree);
        return null;
    }

    private static void crawl(GoogleCoursePlanMetaDataTree googleTree, List<CourseActivityElement> activityElementList) throws ValidException, PersistenceException {
        for (CourseActivityElement courseActivityElement: activityElementList) {
            String googleSheetId = courseActivityElement.getGoogleSheetId();
            if (googleSheetId==null){ //No subElements
                System.out.println(courseActivityElement.getGoogleUniqueId());
                googleTree.getMetaDataMap().put(courseActivityElement.getGoogleUniqueId(),null);
            } else {
                System.out.println(googleSheetId);
                CourseActivityElement googleActivityElement = new GoogleActivityElementController().getGoogleActivityElement(googleSheetId);
                ElementMetaData elementMetaData = new ElementMetaData();
                List<CourseActivitySubElement> subElements = googleActivityElement.getSubElements();
                for (CourseActivitySubElement element: subElements){
                    SubElementMetaData subElementMetaData = new SubElementMetaData();
                    if(element.getGoogleSheetId()!=null) {
                        System.out.println("---" + element.getGoogleSheetId());
                        subElementMetaData.setGoogleUniqueId(element.getGoogleSheetId());
                    } else {
                        System.out.println("---" + element.getGoogleUniqueId());
                        subElementMetaData.setGoogleUniqueId(element.getGoogleUniqueId());
                    }
                    //FIXME: Outdated - discard or fix
                    elementMetaData.getMetaDataList().put("subElementID",subElementMetaData);
                }
                googleTree.getMetaDataMap().put(googleSheetId, elementMetaData);
            }
        }
    }

    public static void main(String[] args) throws ValidException, PersistenceException {
        GoogleCoursePlanCrawler.crawlCoursePlan("1Zj-1eLX67PQRzM7m1icq2vSXzbHn2iFvN4V9cUHTWQo");
    }
}
