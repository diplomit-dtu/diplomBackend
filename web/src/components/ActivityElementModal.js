/**
 * Created by Christian on 30-05-2017.
 */
import React, {Component} from "react";
import PropTypes from 'prop-types';
import {Col, ListGroup, ListGroupItem, Modal, OverlayTrigger, ProgressBar, Row, Tooltip, Well} from "react-bootstrap";
import TextSubElement from "./TextSubElement";
import PopOutLinkSubElement from "./PopOutLinkSubElement";
import EmbeddedLinkSubElement from "./EmbeddedLinkSubelement";
import QuizSubElement from "./QuizSubElement";
import ConceptQuestionSubElement from "./ConceptQuestionSubElement";
import "../index.css";
import CheckboxComp from "./CheckboxComp";
import ProgrammingSubElement from "./ProgrammingSubElement";

export default class ActivityElementModal extends Component {


    constructor(props) {
        super(props);
        this.state={
            html:"noter"
        }
    }

    getSubElementBoxes = () => {
        if (this.props.subElements) {
            return this.props.subElements.map((subElement, index) => {
                if (subElement.subElementType === 'Text') {
                    return <TextSubElement key={index} header={subElement.title} text={subElement.content}
                                           checkBoxId={subElement.id} onCheck={this.handleCheck} checked={subElement.checked}
                                           textBoxId={subElement.id} onChange={this.handleTextBoxChange} notes={subElement.notes}
                    />
                } else if (subElement.subElementType === 'Pop_Out_Link') {
                    return <PopOutLinkSubElement key={index} checkBoxId={subElement.id} checked={subElement.checked} onCheck={this.handleCheck}
                                                 header={subElement.title} link={subElement.hyperLink}
                                                 textBoxId={subElement.id} onChange={this.handleTextBoxChange} notes={subElement.notes}
                    />
                } else if (subElement.subElementType === 'Code') {
                    //TODO move to containerElement
                    return <ProgrammingSubElement key={index} checkBoxId={subElement.id} checked={subElement.checked} onCheck={this.handleCheck}
                                                  header={subElement.title}
                                                  code={subElement.content}
                                                  textBoxId={subElement.id} onChange={this.handleTextBoxChange} notes={subElement.notes}
                    />
                } else if (subElement.subElementType === 'Embedded_Link'){
                    return <EmbeddedLinkSubElement key={index} title={subElement.title}
                                                   checkBoxId={subElement.id} checked={subElement.checked} onCheck={this.handleCheck}
                                                   link={subElement.hyperLink}
                                                   textBoxId={subElement.id} onChange={this.handleTextBoxChange} notes={subElement.notes}
                    />
                } else if (subElement.subElementType === 'Concept_Question') {
                    return <ConceptQuestionSubElement checkBoxId={subElement.id}/>
                } else if(subElement.subElementType==='Quiz') {
                    return <QuizSubElement checkBoxId={subElement.id}/>
                } else { //placeholder for now...
                    return <ListGroupItem key={index} header={subElement.title}>Coming Sooon!</ListGroupItem>
                }

            })
        }
    }

    getMenuElements = () => {
        if (this.props.subElements) {
            return this.props.subElements.map((subelement, index)=>{
                return <li key={index}>


                    <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Afkryds elementet for at markere at det er læst/løst</Tooltip>}>
                        <span><CheckboxComp id={subelement.id} checked={subelement.checked} onCheck={(checked, id)=>this.handleCheck(checked, id)}/></span></OverlayTrigger>{subelement.title}</li>
            })
        }
    }

    scrollToTop = () =>{
        window.scrollTo(0,0);
    };

    hideModal = () =>{
        this.props.hideModal();
    }

    handleCheck = (checked, id) => {
        this.props.handleSubElementCheck(checked, id)
    };

    handleTextBoxChange = (text, id)=>{
        this.props.handleNotes(text,id);
    };


    calculateProgress =() =>{
        var finished = 0.0;
        if (this.props.subElements && Array.isArray(this.props.subElements)) {
            this.props.subElements.forEach((element) => {
                if (element.checked===true)finished++;
            })
            return ((finished / this.props.subElements.length ) * 100).toFixed(0)
        } else {
            return 1
        }
    }


    render() {
        let now = this.calculateProgress();
        let done = (now >= 100);
        return (
            <Modal bsSize="large" dialogClassName="custom-modal" show={this.props.showModal} onHide={this.hideModal} contentLabel="">
                <Modal.Header closeButton><h3 style={{margin:-5}}>{this.props.title}</h3></Modal.Header>


                <Modal.Body>
                    <Row>
                        <Col md={3} sm={4}>
                            <Well style={{cursor: 'pointer', paddingLeft:0,paddingRight:0}}>
                                <ul>
                                    {this.getMenuElements()}
                                </ul>
                                <OverlayTrigger placement={'bottom'} overlay={<Tooltip id={this.props.id}>Viser hvor stor en del af materialet du har læst</Tooltip>}><ProgressBar bsStyle={(now >= 100) ? "success": "info"}  now={now} label={

                                    <span style={{color:"black"}}>{now}%</span>
                                }/></OverlayTrigger>
                            </Well>
                        </Col>
                        {/*Container for contents*/}
                        <Col md={9} sm={8}>
                            <div className="scroll-div">
                                <ListGroup>
                                    {this.getSubElementBoxes()}
                                </ListGroup>
                            </div>
                        </Col>
                    </Row>
                </Modal.Body>
            </Modal>
        )
    }

}

ActivityElementModal.propTypes = {
    subElements: PropTypes.arrayOf(
        PropTypes.shape({
            content: PropTypes.string,
            googleSheetId: PropTypes.string,
            hyperLink: PropTypes.string,
            id: PropTypes.string,
            subElementType: PropTypes.oneOf(['Text', 'Embedded_Link', 'Pop_Out_Link', 'Code', 'Quiz', 'Concept_Question'])
        })),
    handleSubElementCheck: PropTypes.func,
    handleNotes: PropTypes.func
}