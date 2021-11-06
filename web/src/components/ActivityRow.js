/**
 * Created by Christian on 30-05-2017.
 */
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import ActivityElementTD from "./ActivityElementTD";

export default class ActivityRow extends Component{
    handleActivityElementClick = (e, activityElement)=>{
        this.props.handleActivityElementClick(this.props.activity, activityElement);
    };

    getActivityElements() {
        return this.props.activity.activityElementList.map((activityElement, index)=>{
            return <ActivityElementTD key={index} activityElement={activityElement} handleActivityElementClick={this.handleActivityElementClick}/>
        })
    }

    checkIfDone(){
        let done = true;
        this.props.activity.activityElementList.forEach((activityElement)=>{
            if (activityElement.subElements && activityElement.subElements.length>0 && activityElement.progress<1){
                done = false;
            }
        })
        return done;
    }

    render(){
        const date = new Date(this.props.activity.endDate);
        var dateString = ''
        if (this.props.activity.endDate) {
            dateString = date.getDate();
            dateString += '/' + (date.getMonth() + 1);
            if(date.getUTCHours()!==0 || date.getUTCMinutes()!==0) {
                dateString += ' ' + date.getUTCHours();
                dateString += ':' + (date.getUTCMinutes().toString().length === 1 ? '0' : '');
                dateString += date.getUTCMinutes();
            }
        }
        var className = ''
        if (this.checkIfDone()) {
            className += 'success '
        } else if (date > 0 && date < new Date()) {
            className += 'warning'
        }
        if (this.props.activity.status==='DRAFT'){
            className += 'text-muted ';
        }
        return <tr className={className}>
            <td className="td-med">{this.props.activity.title}</td>
            <td className="td-wrap td-wide">{this.props.activity.description}</td>
            <td>{dateString}</td>
            {this.getActivityElements()}

        </tr>
    }



}

ActivityRow.propTypes = {
    activity : PropTypes.shape({
        title: PropTypes.string,
        endDate: PropTypes.any,
        description: PropTypes.string,
        status: PropTypes.oneOf(['VISIBLE','DRAFT', 'INVISIBLE']),
        activityElementList: PropTypes.array,
        handleActivityElementClick: PropTypes.func

    })
}
