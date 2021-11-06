import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Table} from "react-bootstrap";
import ActivityRow from "./ActivityRow";
//import index from '../index.css'

export default class AgendaTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            coursePlan: {courseActivityList:[{activityElementList:[]}]},
            loading :"done"
        }
    }

    handleActivityElementClick = (activity, activityElement)=>{
        this.props.handleActivityElementClick(activity, activityElement);
    };

    getHeaderLine() {
        if (this.props.coursePlan.headers !== null) {
            return <thead><tr>
                {this.props.coursePlan.headers.map((headerTitle, index)=>{
                    return <th key={index}>{headerTitle}</th>
                })}
            </tr>
            </thead>
        } else {
            const activityElementCount = this.props.coursePlan.courseActivityList[0].activityElementList.length;

            return <thead>
            <tr>
                <th>Aktivitet</th>
                <th>Emner</th>
                <th>Tid</th>
                <th colSpan={activityElementCount}>Materiale</th>

            </tr>
            </thead>
        }
    }


    getActivities() {
        return this.props.coursePlan.courseActivityList.map((activity, index)=>{
            return <ActivityRow key={index} activity={activity} handleActivityElementClick={this.handleActivityElementClick}/>
        })
    }




    render() {
        if (this.props.coursePlan && this.props.coursePlan.headers) {
            return (
                <Table hover>

                    {this.getHeaderLine()}
                    <tbody>
                    {this.getActivities()}
                    </tbody>


                </Table>
            )
        } else {
            return (<Table><tbody><tr><td></td></tr></tbody></Table>)
        }



    }


}

AgendaTable.propTypes = {
    user: PropTypes.string,
    handleActivityElementClick: PropTypes.func

}
AgendaTable.defaultProps = {}