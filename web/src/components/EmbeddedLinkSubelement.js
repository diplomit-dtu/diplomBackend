import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Col, Glyphicon, Grid, ListGroupItem, OverlayTrigger, Tooltip} from 'react-bootstrap';
import ContentEditable from "react-contenteditable";
import CheckboxComp from "./CheckboxComp";

export default class EmbeddedLinkSubelement extends Component {


    handleChange = (e) =>{
        this.props.onChange(e.target.value, this.props.textBoxId)
    };


    render(){
        return <ListGroupItem>
            <h4 className="list-group-item-heading">
                <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Afkryds elementet for at markere at det er læst/løst</Tooltip>}><span>
                    <CheckboxComp id={this.props.checkBoxId} onCheck={this.props.onCheck} checked={this.props.checked}/></span></OverlayTrigger>
                {this.props.title} <a href={this.props.link} target="_blank"><span style={{fontSize:'50%'}}>klik for at åbne i et nyt vindue</span> <Glyphicon glyph="new-window" /></a></h4>

            <Grid fluid style={{minHeight:'400px'}}>
                <Col sm={8} style={{}}>
                    <iframe style={{minHeight:'400px',float:'left', width:'100%'}} seamless='seamless' frameBorder={0} src={this.props.link}/>
                </Col>
                <Col sm={4}>
                    <h5>
                        <OverlayTrigger placement={'bottom'} overlay={<Tooltip>Tag noter - noterne gemmes automatisk</Tooltip>}><span>
                            <Glyphicon glyph="pencil"/><b>Noter</b></span></OverlayTrigger>
                    </h5>
                    <ContentEditable style={{borderWidth:1, borderStyle:"solid", borderColor:"lightgrey"}} html={this.props.notes} onChange={this.handleChange}/>
                </Col>
            </Grid>

        </ListGroupItem>
    }

}

EmbeddedLinkSubelement.propTypes= {
    notes: PropTypes.string,
    onChange: PropTypes.func,
    link: PropTypes.string,
    onCheck: PropTypes.func

}
