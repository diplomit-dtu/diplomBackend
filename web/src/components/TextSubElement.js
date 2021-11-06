/**
 * Created by Christian on 31-05-2017.
 */
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Col, Glyphicon, Grid, ListGroupItem, OverlayTrigger, Row, Tooltip} from "react-bootstrap";
import ContentEditable from "react-contenteditable";
import CheckboxComp from "./CheckboxComp";

export default class TextSubElement extends Component{
    handleChange =  (e)=>{
        this.props.onChange(e.target.value, this.props.textBoxId)
};
    constructor(props){
        super(props)

    }

    render(){
        return (
            <ListGroupItem>
                <h4 className="list-group-item-heading">
                    <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Afkryds elementet for at markere at det er læst/løst</Tooltip>}><span>
                    <CheckboxComp id={this.props.checkBoxId} checked={this.props.checked} onCheck={this.props.onCheck}/>
                    </span>
                    </OverlayTrigger>
                    {this.props.header}
                </h4>

                <Grid fluid>
                    <Row>
                        <Col sm={8}>

                            {this.props.text}
                        </Col>
                        <Col sm={4}>
                            <h5>
                                <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Tag noter - noterne gemmes automatisk</Tooltip>}><span>
                                    <Glyphicon glyph="pencil"/><b>Noter</b></span></OverlayTrigger>
                            </h5>
                            <ContentEditable style={{borderWidth:1, borderStyle:"solid", borderColor:"lightgrey"}} html={this.props.notes} onChange={this.handleChange}/>

                        </Col>
                    </Row>
                </Grid>
            </ListGroupItem>
        )
    }
}

TextSubElement.propTypes={
    textBoxId: PropTypes.string,
    notes: PropTypes.string,
    onChange: PropTypes.func
}