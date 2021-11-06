/**
 * Created by Christian on 30-05-2017.
 */
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {OverlayTrigger, ProgressBar, Tooltip} from "react-bootstrap";
import {styles} from "../index";


export default class ActivityElementTD extends Component {
    handleActivityElementClick = (e)=>{

        this.props.handleActivityElementClick(e,this.props.activityElement);

    };

    render() {
        if (this.props.activityElement.activityElementType!=='Text') {
            return this.linkElement()
        } else {
            return (<td className="td-wrap">{this.props.activityElement.title}</td>)
        }
    }


    linkElement() {
        let now = (Number(this.props.activityElement.progress) * 100).toFixed(0);
        now = +now || 0; //Convert NaN to 0
        let showProgress = this.props.activityElement.subElements.length > 0;
        return (<td className="td-wrap">
            <div style={styles.a} target="_blank" onClick={(e)=>this.handleActivityElementClick(e)} id={this.props.activityElement.id}>
                {this.props.activityElement.title} </div>
            {showProgress &&  <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Viser hvor stor en del af materialet du har læst</Tooltip>}><ProgressBar bsStyle={(now >= 100) ? "success": "info"}  now={now} label={
                <span style={{color:"black"}}>{now}%</span>
            }/></OverlayTrigger>}
        </td>);
    }

}

ActivityElementTD.propTypes = {
    activityElement: PropTypes.shape({
        activityElementType: PropTypes.oneOf(['Link', 'Native', 'GoogleSheet', 'Text']),
        hyperLink: PropTypes.string,
        text: PropTypes.string,
        handleActivityElementClick: PropTypes.func
    })
}

