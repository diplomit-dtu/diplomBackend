/**
 * Created by Christian on 01-08-2017.
 */
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Glyphicon, Panel} from "react-bootstrap";
import ripple from '../ripple.svg'

export default class CourseAdminMenu extends Component{
    handleCourseSelection= (courseId)=>{
        this.props.courseClicked(courseId)
    };

    handleNewCourseClick= ()=>{
        this.props.newCourseClicked();
    }

    generateCourseList(){
        if (this.props.loading===true) {
            return <img alt="loading" src={ripple}/>
        } else {
            return <ul>
                {this.props.courseList.map((course) => {
                    return <li key={course.id}><a
                        onClick={() => this.handleCourseSelection(course.id)}>{course.courseShortHand} - {course.courseName}</a>
                    </li>
                })}
            </ul>
        }
    }

    render(){
        return <div>
            <Panel>
                <div className="panel-title">Kursus Administration</div>
                <div className="panel-body">
                    {this.generateCourseList()}

                    {this.props.createCourses && <a onClick={this.handleNewCourseClick}><Glyphicon glyph="plus"/> Nyt kursus</a>}
                </div>
            </Panel>
        </div>
    }
}

CourseAdminMenu.propTypes = {
    courseList: PropTypes.array,
    courseClicked: PropTypes.func,
    newCourseClicked: PropTypes.func,
    createCourses: PropTypes.bool,
    loading: PropTypes.any
}

CourseAdminMenu.defaultProps = {
    courseList: []
}