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
import business.interfaces.ValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private CoursePlan deepParse(CoursePlan googleFetchedCoursePlan) {
        System.out.println("---------------------\r\n DeepParse\r\n ---------------");
        System.out.println("activities Fetched: " + googleFetchedCoursePlan.getCourseActivityList().size());
        List<CourseActivity> courseActivityList = googleFetchedCoursePlan.getCourseActivityList();
        //New courseActivityList for fetched CourseActivities
        List<CourseActivity> deepParsedCourseActivityList = new ArrayList<>();

        //Allocate space in list for right number of elements
        for (int i = 0; i < courseActivityList.size(); i++)
            deepParsedCourseActivityList.add(null);

        List<Thread> threadList = new ArrayList<>();
        int activityIndex = 0;
        //Iterate over Activities in original plan
        for (CourseActivity courseActivity: courseActivityList) {
            //Get ActivityElements from original activities
            List<CourseActivityElement> activityElementList = courseActivity.getActivityElementList();
            //New CourseActivityElementList for fetched ActivityElements
            List<CourseActivityElement> deepParsedCourseActivityElementList = new ArrayList<>();

            class ActivityThread extends Thread {
                private final int activityIndex;
                private final CourseActivity courseActivity;

                @Override
                public void run() {
                    System.out.println("courseActivity started: " + courseActivity.getTitle());

                    //Allocate space in list for right number of elements
                    for (int i = 0; i < activityElementList.size(); i++)
                        deepParsedCourseActivityElementList.add(null);

                    List<Thread> threadList = new ArrayList<>();
                    int elementIndex = 0;
                    //Iterate over ActivityElements in original plan
                    for (CourseActivityElement templateCourseActivityElement : activityElementList) {
                        class ActivityElementThread extends Thread {
                            private CourseActivityElement courseActivityElement;
                            private CourseActivityElement fetchedCourseActivityElement;
                            private final int index;

                            @Override
                            public void run() {
                                Spreadsheet activityElementSheet = null;
                                //try to fetch SubSheet
                                try {
                                    activityElementSheet = googleSheetsDAO.getSheet(courseActivityElement.getGoogleSheetId());
                                    fetchedCourseActivityElement = GoogleActivityElementParser.parseSheet(activityElementSheet);
                                    fetchedCourseActivityElement.setGoogleSheetId(courseActivityElement.getGoogleSheetId());
                                    //Write the title back...
                                    fetchedCourseActivityElement.setTitle(courseActivityElement.getTitle());
                                    deepParsedCourseActivityElementList.set(index, fetchedCourseActivityElement);
                                    System.out.println("-------------------------\r\n" + templateCourseActivityElement);
                                    System.out.println(templateCourseActivityElement);
                                } catch (PersistenceException e) {
                                    //SubSheetUnavailable!
                                    CourseActivityElement unavailableElement = templateCourseActivityElement;
                                    unavailableElement.setActivityElementType(CourseActivityElement.ActivityElementType.Unavailable);
                                    deepParsedCourseActivityElementList.set(index, unavailableElement);
                                }
                            }

                            private ActivityElementThread(int index, CourseActivityElement courseActivityElement) {
                                this.index = index;
                                this.courseActivityElement = courseActivityElement;
                            }
                        }

                        //Check if ActivityElement has a sheetId - meaning it has a subsheet
                        if (templateCourseActivityElement.getGoogleSheetId() != null) {
                            //Create new thread
                            threadList.add(new ActivityElementThread(elementIndex, templateCourseActivityElement));
                        } else {
                            //Add element to list at right index
                            deepParsedCourseActivityElementList.set(elementIndex, templateCourseActivityElement);
                        }
                        elementIndex++;
                    }

                    startAndJoinThreads(threadList);

                    courseActivity.setActivityElementList(deepParsedCourseActivityElementList);
                    deepParsedCourseActivityList.set(activityIndex,courseActivity);
                    System.out.println("courseActivity ended: " + courseActivity.getTitle());
                }

                private ActivityThread(int index, CourseActivity courseActivity) {
                    this.activityIndex = index;
                    this.courseActivity = courseActivity;
                }
            }
            //Create new thread
            threadList.add(new ActivityThread(activityIndex, courseActivity));
            activityIndex++;
        }

        startAndJoinThreads(threadList);

        System.out.println("DeepParsedActivities: " + deepParsedCourseActivityList.size());
        googleFetchedCoursePlan.setCourseActivityList(deepParsedCourseActivityList);
        return googleFetchedCoursePlan;
    }

    private void startAndJoinThreads(List<Thread> threadList){
        //Start all threads
        for (Thread t : threadList)
            t.start();

        //Wait for all threads to finish
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveActivityList(List<CourseActivity> courseActivityList, List<CourseActivity> oldActivityList) throws PersistenceException, ValidException {
        for (CourseActivity activity : courseActivityList) {
            for (CourseActivity oldCourseActivity : oldActivityList) {
                //If either title or description is the same as one from last syncronization assume that they are the same
                if ((oldCourseActivity.getDescription()!=null && oldCourseActivity.getTitle()!=null) &&
                        (Objects.equals(oldCourseActivity.getDescription(), activity.getDescription()) ||
                                Objects.equals(oldCourseActivity.getTitle(), activity.getTitle()))){
                    activity.setId(oldCourseActivity.getId());
                    //Compare Elements
                    compareElements(activity, oldCourseActivity);

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
            if (!used){
                //Cascade Delete - sigh!
                List<CourseActivityElement> activityElementList = oldActivity.getActivityElementList();
                for (CourseActivityElement courseActivityElement: activityElementList ){
                    for (CourseActivitySubElement subElement: courseActivityElement.getSubElements()){
                        courseActivitySubElementDAO.delete(subElement.getId());
                    }
                    courseActivityElementDAO.delete(courseActivityElement.getId());
                }

                courseActivityDAO.delete(oldActivity.getId());
            } else {
                //TODO cleanup Elements - sigh! (elements are leaked - not a big problem (few kb)- but should be fixed
            }

        }

    }

    private void compareElements(CourseActivity activity, CourseActivity oldCourseActivity) throws PersistenceException {
        List<CourseActivityElement> newActivityElementList = activity.getActivityElementList();
        List<CourseActivityElement> oldActiviyElementList = oldCourseActivity.getActivityElementList();
        //Loop through both lists looking for identical id's //TODO refactor to map
        for(CourseActivityElement newCourseActivityElement: newActivityElementList){
            for(CourseActivityElement oldCourseActivityElement: oldActiviyElementList){
             if (newCourseActivityElement.getSubElements()!=null && newCourseActivityElement.getSubElements().size()>0 &&
                     newCourseActivityElement.getGoogleSheetId()!=null &&
                     Objects.equals(newCourseActivityElement.getGoogleSheetId(), oldCourseActivityElement.getGoogleSheetId()) ){
                 //SubElement is the same
                 newCourseActivityElement.setId(oldCourseActivityElement.getId());
                 compareSubElements(newCourseActivityElement, oldCourseActivityElement);
             }
            }
            for(CourseActivitySubElement subElement: newCourseActivityElement.getSubElements()){
                courseActivitySubElementDAO.save(subElement);
            }
            courseActivityElementDAO.save(newCourseActivityElement);
        }
    }

    private void compareSubElements(CourseActivityElement newCourseActivityElement, CourseActivityElement oldCourseActivityElement) throws PersistenceException {
        List<CourseActivitySubElement> newSubElements = newCourseActivityElement.getSubElements();
        List<CourseActivitySubElement> oldSubElements = oldCourseActivityElement.getSubElements();
        for (CourseActivitySubElement newSubElement : newSubElements){
            for(CourseActivitySubElement oldSubElement : oldSubElements){
                if (Objects.equals(newSubElement.getContent(), oldSubElement.getContent()) || Objects.equals(newSubElement.getTitle(), oldSubElement.getTitle())){
                    //Subelements are assumed to be the same - reuse id
                    newSubElement.setId(oldSubElement.getId());
                }
            }
        }
    }

    private void saveActivity(CourseActivity courseActivity) throws PersistenceException {
        List<CourseActivityElement> activityElementList = courseActivity.getActivityElementList();
        //Cascade Save... sigh!
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
