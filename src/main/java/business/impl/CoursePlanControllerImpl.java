package business.impl;

import business.interfaces.CoursePlanController;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import data.dbDTO.CourseActivity;
import data.dbDTO.CourseActivityElement;
import data.dbDTO.CourseActivitySubElement;
import data.dbDTO.CoursePlan;
import data.googleImpl.GoogleSheetsDAOImpl;
import data.interfaces.*;
import data.mongoImpl.MongoCourseActivityDAO;
import data.mongoImpl.MongoCourseActivityElementDAO;
import data.mongoImpl.MongoCourseActivitySubElementDAO;
import data.mongoImpl.MongoCoursePlanDAO;
import rest.ValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 06-08-2017.
 */
public class CoursePlanControllerImpl implements CoursePlanController {
    private CoursePlanDAO coursePlanDAO = new MongoCoursePlanDAO();
    private CourseActivityDAO courseActivityDAO = new MongoCourseActivityDAO();
    CourseActivityElementDAO courseActivityElementDAO = new MongoCourseActivityElementDAO();
    CourseActivitySubElementDAO courseActivitySubElementDAO = new MongoCourseActivitySubElementDAO();
    private GoogleSheetsDAO googleSheetsDAO = new GoogleSheetsDAOImpl();
    @Override
    public void syncCoursePlan(String googleSheetPlanId, String coursePlanId) throws ValidException, PersistenceException {

        CoursePlan coursePlan = coursePlanDAO.get(coursePlanId);
        //Making sure there is a coursePlan
        if (coursePlan==null){
            coursePlan = new CoursePlan();
            coursePlan.setId(coursePlanId);
        }
        Spreadsheet sheet = googleSheetsDAO.getSheet(googleSheetPlanId);
        CoursePlan googleFetchedCoursePlan = GoogleCoursePlanParser.parseCoursePlanFromSheet(sheet);
        CoursePlan deepParsedPlan = deepParse(googleFetchedCoursePlan);

        deepParsedPlan.setId(coursePlanId);
        List<CourseActivity> courseActivityList = deepParsedPlan.getCourseActivityList();
        saveActivityList(courseActivityList, coursePlan.getCourseActivityList());
        coursePlanDAO.save(googleFetchedCoursePlan);


        CoursePlan reloaded = coursePlanDAO.get(coursePlanId);
        System.out.println("reloaded: " + reloaded);

    }

    private CoursePlan deepParse(CoursePlan googleFetchedCoursePlan) throws PersistenceException {
        System.out.println("---------------------\r\n DeepParse\r\n ---------------");
        System.out.println("activities Fetched: " + googleFetchedCoursePlan.getCourseActivityList().size());
        List<CourseActivity> courseActivityList = googleFetchedCoursePlan.getCourseActivityList();
        List<CourseActivity> deepParsedCourseActivityList = new ArrayList<>();
        for (CourseActivity courseActivity: courseActivityList) {
            List<CourseActivityElement> activityElementList = courseActivity.getActivityElementList();
            List<CourseActivityElement> deepParsedCourseActivityElementList = new ArrayList<>();
            for (CourseActivityElement courseActivityElement: activityElementList){
                String title =courseActivityElement.getTitle();
                if (courseActivityElement.getGoogleSheetId()!=null) {
                    Spreadsheet activityElementSheet = googleSheetsDAO.getSheet(courseActivityElement.getGoogleSheetId());
                    CourseActivityElement fetchedGoogleCourseActivityElement = GoogleActivityElementParser.parseSheet(activityElementSheet);
                    courseActivityElement = fetchedGoogleCourseActivityElement;
                    System.out.println("-------------------------\r\n" + courseActivityElement);
                    System.out.println(courseActivityElement);
                    courseActivityElement.setTitle(title);

                }
                deepParsedCourseActivityElementList.add(courseActivityElement);
            }
            courseActivity.setActivityElementList(deepParsedCourseActivityElementList);
            deepParsedCourseActivityList.add(courseActivity);
        }
        System.out.println("DeepParsedActivities: " + deepParsedCourseActivityList.size());
        googleFetchedCoursePlan.setCourseActivityList(deepParsedCourseActivityList);
        return googleFetchedCoursePlan;
    }

    private void saveActivityList(List<CourseActivity> courseActivityList, List<CourseActivity> oldActivityList) throws PersistenceException, ValidException {
        for (CourseActivity activity : courseActivityList) {
            for (CourseActivity oldCourseActivity : oldActivityList) {
                //If either title or description is the same as one from last syncronization assume that they are the same
                if (oldCourseActivity.getDescription().equals(activity.getDescription()) ||
                        oldCourseActivity.getTitle().equals(activity.getTitle())){
                    activity.setId(oldCourseActivity.getId());
                }
            }
            saveActivity(activity);
        }
        //Clean up orphans O(n)2 - Will perform poorly if there are many elements in a courseplan
        for (CourseActivity oldActivity : oldActivityList){
            boolean used = false;
            //Iterate over new list to see if the old activity was reused
            for (CourseActivity newCourseActivity: courseActivityList){
                if (oldActivity.getId().equals(newCourseActivity.getId())){
                    used = true;
                }
            }
            if (used==false){
                //Cascade Delete - sigh!
                List<CourseActivityElement> activityElementList = oldActivity.getActivityElementList();
                for (CourseActivityElement courseActivityElement: activityElementList ){
                    for (CourseActivitySubElement subElement: courseActivityElement.getSubElements()){
                        courseActivitySubElementDAO.delete(subElement.getId());
                    }
                    courseActivityElementDAO.delete(courseActivityElement.getId());
                }

                courseActivityDAO.delete(oldActivity.getId());
            }

        }

    }

    private void saveActivity(CourseActivity courseActivity) throws PersistenceException {
        List<CourseActivityElement> activityElementList = courseActivity.getActivityElementList();
        for (CourseActivityElement courseActivityElement: activityElementList) {
            for (CourseActivitySubElement subElement: courseActivityElement.getSubElements()){
                subElement = courseActivitySubElementDAO.save(subElement);
            }
            courseActivityElementDAO.save(courseActivityElement);
        }
        courseActivityDAO.save(courseActivity);
    }

    @Override
    public CoursePlan createCoursePlan() throws PersistenceException {
        CoursePlan coursePlan= new CoursePlan();
        CoursePlan save = coursePlanDAO.save(coursePlan);
        return save;
    }
}
