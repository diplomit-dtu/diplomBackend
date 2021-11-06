import React, {Component} from "react";
import PropTypes from 'prop-types';
import {Col, Grid, Row} from "react-bootstrap";
import AgendaTable from "./components/AgendaTable";
import ripple from './ripple.svg';
import ActivityElementModal from "./components/ActivityElementModal";

export default class Agenda extends Component {


    constructor(props) {
        super(props)
        //Set initialState
        this.state = {
            showModal: false,
            linksUrl: this.props.apiUrl + this.props.linksPath,
            activityElementUrl: this.props.apiUrl + this.props.activityElementsPath,
            activitySubElements: [],
            activeActivityElement: ""
        }

    }

    handleActivityClick = (activity, activityElement) => {
        this.props.handleActivityClick(activity,activityElement);

    };

    hideModal = (e) =>{
        this.props.hideModal();
    };

    handleSubElementCheck = (checked, id)=>{
        this.props.handleSubElementCheck(checked, this.props.activeActivityId, this.props.activeActivityElementId, id);
    }

    handleNotes = (text, id)=>{
        this.props.handleSubElementNotes(text, this.props.activeActivityId, this.props.activeActivityElementId, id);
    }

//view
    render() {
        //TODO: fremmøderegistrering
        // let courseAdmin = false;
        // if (this.props.course.admins && this.props.user && this.props.user.id){
        //    this.props.course.admins.forEach((adminId)=>{
        //        courseAdmin = (adminId===this.props.user.id);
        //    })
        // }
        return (
            <Grid>
                {this.props.coursePlanLoading===true && <div>Loading courseplan... <img src={ripple} alt="..."/></div>}
                {this.props.coursePlanLoading==="failed" && <div>Kursusplanen kunne ikke indlæses</div>}
                <Row>
                    <Col>
                        <AgendaTable coursePlan={this.props.coursePlan}
                                     handleActivityElementClick={this.handleActivityClick}/>
                    </Col>
                </Row>


                        <ActivityElementModal hideModal={this.hideModal} showModal={this.props.showModal}
                                              className="scroll-div" ref="activityContainer" title={this.props.activeActivityElement}
                                              handleSubElementCheck={this.handleSubElementCheck}
                                              handleNotes={this.handleNotes}
                                              subElements={this.props.activitySubElements}/>



            </Grid>)

    }

}
Agenda.propTypes = {
    apiUrl: PropTypes.string,
    linksPath: PropTypes.string,
    generalLinksTitle: PropTypes.string,
    courseLinksTitle: PropTypes.string,
    courseplansPath: PropTypes.string,
    activityElementsPath: PropTypes.string,
    googlepath: PropTypes.string,
    user: PropTypes.string,
    coursePlan: PropTypes.any,
    activeActivityElement: PropTypes.any,
    course: PropTypes.shape({
        id: PropTypes.string,
        text: PropTypes.string,
        coursePlanId: PropTypes.string,
        coursePlanSource: PropTypes.oneOf(['GoogleSheet', 'Mongo'])
    }).isRequired,
    handleActivityClick: PropTypes.func,
    handleSubElementCheck: PropTypes.func,
    handleSubElementNotes: PropTypes.func,
    hideModal: PropTypes.func
}

Agenda.defaultProps = {
    apiUrl: '',
    user: null,
    linksPath: '/links',
    courseplansPath: '/courseplans',
    activityElementsPath: '/activityelements',
    googlepath: '/google',
    generalLinksTitle: 'Fælles links',
    courseLinksTitle: 'Kursus links'

}