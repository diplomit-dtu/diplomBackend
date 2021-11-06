/**
 * Created by Christian on 17-05-2017.
 */

import {Popover, OverlayTrigger, Button, Form, FormControl} from 'react-bootstrap';
import React, {Component} from 'react'

export default class NewLink extends Component {
    constructor(props) {
        super(props);
        this.state = {text: "", href: ""}
    }


    handleHrefChange = (e) => {
        this.setState({href:e.target.value})
    }
    handleTextChange = (e) => {
        this.setState({text:e.target.value})
    }

    handleButtonClick = (e) => {
        this.refs.overlay.hide();
        const newLink = {text:this.state.text,href:this.state.href}
        this.props.onSubmitLink(newLink)
    }


    render() {
        const popover = (
            <Popover id="modal-popover" title="Nyt Link">
                <Form>
                    <FormControl placeholder="titel" value={this.state.text} onChange={this.handleTextChange}/>
                    <FormControl placeholder="url" value={this.state.href} onChange={this.handleHrefChange}/>
                    <Button onClick={this.handleButtonClick}/>
                </Form>
            </Popover>)
        return (
            <div>
                <OverlayTrigger ref="overlay" trigger="click" placement="bottom" overlay={popover}>
                    <Button>Nyt link</Button>
                </OverlayTrigger>
            </div>)
    }

}

NewLink.propTypes={
    onSubmitLink: React.PropTypes.func.isRequired
}
