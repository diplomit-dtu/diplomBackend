/**
 * Created by Christian on 29-06-2017.
 */
import React,{Component} from 'react'
import {Col, Glyphicon, Grid, ListGroupItem, Row} from "react-bootstrap";
import ContentEditable from "react-contenteditable";

export default class ConceptQuestionSubElement extends Component{

    render(){
        return<ListGroupItem>
            <h4><input id={"check"+this.props.checkBoxId} type="checkbox" defaultChecked={true}/><label htmlFor={"check" + this.props.checkBoxId}> </label>
                ConceptQuestion</h4>
            <Grid fluid>
                <Row>
                    <Col sm={8}>
                        {this.props.text}
                    </Col>
                    <Col sm={4}>
                        <h5><Glyphicon glyph="pencil"/><b>Noter</b></h5>
                        <ContentEditable/>
                    </Col>
                </Row>
            </Grid>
        </ListGroupItem>
    }
}