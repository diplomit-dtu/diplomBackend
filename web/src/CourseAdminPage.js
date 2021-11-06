/**
 * Created by Christian on 01-08-2017.
 */

import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Col, Grid, Row} from "react-bootstrap";
import CourseAdminMenu from "./components/CourseAdminMenu";
import Rip from "./rest/Rip";
import CourseAdminMain from "./components/CourseAdminMain";
import {toast} from "react-toastify";

export default class CourseAdminPage extends Component{
    constructor(props){
        super(props);
        //TODO clean up
        this.getCourses();
        this.state = {
            loading: true,
            courseList: [],
            currentCourse : this.props.course
        }

    }

    getCourses() {
        Rip.getJson(this.props.apiUrl + this.props.coursePath + "/owned", this.courseCallBack, this.courseCatchBack)
    }
    courseCallBack = (json)=> {

        let newCurrentCourse;
        if (this.state.currentCourse == null) {
            this.setState({
                courseList: json,
                currentCourse: json[0]
            })
        } else {
            let currentCourseId = this.state.currentCourse.id;
            newCurrentCourse = json[0];
            json.forEach((course, index) => {
                if (course.id === currentCourseId) {
                    newCurrentCourse=course;
                }
            })
            this.setState({
                courseList: json,
                currentCourse: newCurrentCourse,
                syncError: false
            });
        }
        this.fetchUserList(this.state.currentCourse.id);

    }

    courseCatchBack = (data) =>{
        this.setState({loading:"failed"})
    }

    courseSelected = (courseId) =>{
        var foundCourses = this.state.courseList.filter((course)=>{
            return course.id===courseId;
        })
        this.fetchUserList(courseId);
        this.setState({
            currentCourse: foundCourses[0]
        });
    };

    fetchUserList = (courseId) =>{
        this.setState({loading:true});
        Rip.getJson(this.props.apiUrl + this.props.coursePath + '/' + courseId +'/users/list',(users)=> {
            this.setState({users:users, loading: 'done'});
        }, null) //TODO: Catchback
    }
    newCourseSelected = () =>{
        this.setState({loading:true})
        Rip.post(this.props.apiUrl + this.props.coursePath,null,
            (json)=>{
                let courseList = this.state.courseList;
                courseList.push(json);
                this.setState({loading:"done", courseList:courseList})
            },
            (errorMsg)=>{
                this.setState({loading:"failed"})
                console.log(errorMsg);
            })
    };





    handleRoleCheck(userId, role, value) {
        Rip.put(this.props.apiUrl + this.props.coursePath + '/' + this.state.currentCourse.id + '/users/' + userId + '/' + role, value,
            (json)=>{
                this.fetchUserList(this.state.currentCourse.id)
            },
            null)
    }
    addUserToCourse(userName, name, email){
        console.log(userName)
        toast.success("Bruger " + userName + " tilføjet", {autoClose: 8000});
        let userUpdate = {userName: userName, name: name, email: email, role: "student"}
        Rip.postForString(this.currentCourseUrl() + '/users/', userUpdate,
            (String)=>{
                this.fetchUserList(this.state.currentCourse.id)
            },
            null)
    }

    currentCourseUrl() {
        return this.props.apiUrl + this.props.coursePath + '/' + this.state.currentCourse.id;
    }

    addUserCSVToCourse(csvString) {
        let usersString = {usersCsv:csvString};
        this.toastLoading = toast("Tilføjer brugere...", {autoClose: false});
        Rip.postForString(this.currentCourseUrl() + '/users/csv', usersString,
            (String)=>{
                toast.dismiss(this.toastLoading);
                toast.success("Brugere tilføjet", {autoClose: 5000});
                this.fetchUserList(this.state.currentCourse.id);
            })
    }


    updateCourseShortAndName(short, name) {
        let shortAndName = {shortHand:short,courseName: name};
        Rip.postForString(this.currentCourseUrl() + "/name", shortAndName,
            (String)=>{
                this.getCourses()
            },(response)=>{
                response.response.text().then((text)=>{
                    toast.warn(response.status + ": " + text)
                })
            }

        )
    }

    updateCourseUsesGoogleSheet = (checked)=> {
        let usesGoogleSheet = {usesGoogleSheet: checked}
        Rip.postForString(this.currentCourseUrl() + '/usesGoogleSheet', usesGoogleSheet,
            (String)=>{
                this.getCourses()
            })
    }

    newGoogleSheetId = (id)=>{
        let googleSheetId = {googleSheetId:id}
        Rip.postForString(this.currentCourseUrl() + '/googleSheetId', googleSheetId,
            (String)=>{
                this.getCourses()
            })
    }
    syncCourseCurrentCoursePlan = ()=> {
        this.setState({syncing:true})
        Rip.postForString(this.currentCourseUrl() + '/syncCoursePlan', null,
            (response)=>{

                if (response.status===500){
                    response.text().then((text)=>{
                        this.setState({syncError: text})
                    })
                    this.setState({syncError: response.text, syncing:false})
                } else {
                    this.getCourses();
                    this.setState({syncError: false, syncing:false})
                }
            },
            (response)=>{
                response.response.text().then((text)=>
                {
                    this.setState({syncError: text, syncing: false})
                })
            })
    }


    checkForCourseAdmin = ()=> {
        let isCourseAdmin = false;
        if (this.props.user && this.props.user.roles){
            this.props.user.roles.forEach((role)=>{
                if (role.roleName ==="CourseAdmin"){
                    isCourseAdmin=true;
                }
            })
        }
        return isCourseAdmin;
    }

    render(){
        let userIsCourseAdmin = this.checkForCourseAdmin();
        console.log("User is course admin: " + userIsCourseAdmin)
        return <Grid fluid>

            <Row>
                <Col md={3}>
                    <CourseAdminMenu createCourses={userIsCourseAdmin} loading={this.state.loading} courseClicked={this.courseSelected} newCourseClicked={this.newCourseSelected} courseList={this.state.courseList}/>
                </Col>
                <Col md={9}>
                    <CourseAdminMain course={this.state.currentCourse} users={this.state.users}
                                     loading={this.state.loading}
                                     newUserAdded={(userName, name, email)=>this.addUserToCourse(userName, name, email)}
                                     newUserCSVSubmitted={(csvString)=>this.addUserCSVToCourse(csvString)}
                                     newShortAndTitle={(short,name)=>this.updateCourseShortAndName(short,name)}
                                     usesGoogleSheet={(checked)=>this.updateCourseUsesGoogleSheet(checked)}
                                     newGoogleSheetId={(sheetId)=>this.newGoogleSheetId(sheetId)}
                                     roleChecked={(userId,role,value)=>this.handleRoleCheck(userId,role,value)}
                                     syncError={this.state.syncError}
                                     syncCoursePlan={()=>this.syncCourseCurrentCoursePlan()}
                                     syncing={this.state.syncing}/>
                </Col>
            </Row>
        </Grid>

    }



}

CourseAdminPage.propTypes = {
    apiUrl: PropTypes.string,
    coursePath: PropTypes.string,
    course: PropTypes.any,
    user: PropTypes.any

}

CourseAdminPage.defaultProps = {
    apiUrl: '',
    coursePath: '/courses'
}