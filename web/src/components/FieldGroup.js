/**
 * Created by Christian on 02-08-2017.
 */

import React, {Component} from 'react'
import {ControlLabel, FormControl, FormGroup, HelpBlock} from "react-bootstrap";

export default class FieldGroup extends Component{

    render(){
       return  <FormGroup>
            <ControlLabel>
                {this.props.label}
            </ControlLabel>
            <FormControl {...this.props}></FormControl>
            {this.props.help && <HelpBlock>{this.props.help}</HelpBlock>}
        </FormGroup>
    }

}