/**
 * Created by Christian on 31-05-2017.
 */
import React, {Component} from 'react'
import {Col, Glyphicon, Grid, ListGroupItem, OverlayTrigger, Tooltip} from "react-bootstrap";
import CheckboxComp from "./CheckboxComp";
import ContentEditable from "react-contenteditable";

export default class PopOutLinkSubElement extends Component{
    itemClicked = ()=>{
        window.open(this.props.link)
    };

    handleChange = (e) =>{
        this.props.onChange(e.target.value, this.props.textBoxId)
    };

    render(){
        console.log()
        return (<ListGroupItem>
            <h4>
                <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Afkryds elementet for at markere at det er læst/løst</Tooltip>}><span>
                    <CheckboxComp id={this.props.checkBoxId} checked={this.props.checked} onCheck={this.props.onCheck}/></span></OverlayTrigger>
                <a onClick={this.itemClicked}>{this.props.header} <Glyphicon glyph="new-window" /></a></h4>

            <Grid fluid>
                <Col sm={8} style={{}}>

                </Col>
                <Col sm={4}>
                    <h5>
                        <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Tag noter - noterne gemmes automatisk</Tooltip>}><span>
                            <Glyphicon glyph="pencil"/><b>Noter</b></span></OverlayTrigger>
                    </h5>
                    <ContentEditable style={{borderWidth:1, borderStyle:"solid", borderColor:"lightgrey"}} html={this.props.notes} onChange={this.handleChange}/>
                </Col>
            </Grid>
        </ListGroupItem>)
    }

}
