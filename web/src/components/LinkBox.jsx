/** Linkbox component for creating new Links
 *
 */

import React, {Component} from "react";
import {Button, Col, Glyphicon, Row, Well} from 'react-bootstrap'
import ripple from '../ripple.svg';
import NewLink from "./NewLink";
import Rip from '../rest/Rip'
export default class LinkBox extends Component {

    constructor(props) {
        super(props);
        this.state = {
            links: [],
            loading: true,
            newlink: {text: "", href: ""},
            editmode: false
        }
        console.log(this.state);
        this.fetchLinks();
    }

    fetchLinks = () => {
        Rip.getJson(this.props.linkUrl,(json)=>{
            this.setState({links:json, loading:"done"});
        }, (message)=>{
            this.setState({links:[], loading:"fail"});
            console.log(message);
        })

        // fetch(this.props.linkUrl, {mode: 'cors'}).then((response) => {
        //     response.json().then((json) => {
        //         this.setState({links: json, loading: "done"});
        //
        //     }).catch((response) => {
        //         this.setState({links: [], loading: "fail"});
        //         console.log("Something went wrong fetching urls from: " + this.props.linkUrl + " error: " + response);
        //     });
        // });
    }

    handleNewLink = (link) => {
        console.log("posting new lilnk");
        console.log(link);
        const dummyId = Math.random().toString(36).substring(7);
        const newLinks = this.state.links.concat([{id: dummyId, text: link.text, href: link.href}])
        this.setState({links: newLinks, newlink: {id: dummyId, text: '', href: ''}});
        fetch(this.props.linkUrl, {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(link),
            headers: new Headers({
                'content-type': 'application/json'
            })
        }).then((response) => {
            response.json().then((json) => {
                    console.log('got reply!')
                    var oldState = Object.assign({}, this.state);
                    oldState.links.forEach((link) => {
                        console.log('linkid' + link.id)
                        if (link.id === dummyId) {
                            link.id = json.id;
                            console.log('found link!')
                        }
                    })
                    this.setState(oldState);
                }
            )
        }).catch((reponse) => {
            console.log("something went wrong while posting to " + this.props.linkUrl + "error: " + reponse)
        });
        console.log(this.state);
    };

    handleRemoveLink = (e) => {
        console.log(e.target.id);
        const removeId = e.target.id;
        fetch(this.props.linkUrl + "/" + e.target.id, {
            mode: 'cors',
            method: 'DELETE',
            contentType: 'application/json'
        }).then(() => {
            var updatedLinks = [];
            this.state.links.forEach((link) => {
                console.log(link);
                if (link.id === removeId) {
                    console.log("Found item to remove!");
                } else {
                    updatedLinks.push(link);
                }
            })
            this.setState({links: updatedLinks})
        });
    };

    handleEditClick = (e) => {
        const newEditMode = !this.state.editmode
        this.setState({editmode: newEditMode})
    };


    getLinks = () => {
        return (
            this.state.links.map((link) => {
                console.log("link " + link);
                return <li key={link.id}><a href={link.href}>{link.text}</a> {this.state.editmode &&
                <span><Glyphicon glyph="pencil"/><Glyphicon onClick={this.handleRemoveLink} id={link.id}
                                                            glyph="remove"/></span>}</li>
            })
        )
    }
//View
    render() {
        if (this.state.loading === "done") {
            return (

                <Well>
                    <Row>
                        <Col xs={8} xsOffset={2}><h4 className="text-center">{this.props.title}</h4></Col>
                        <Col xs={2}><Button bsSize="xsmall" onClick={this.handleEditClick} active={this.state.editmode}><Glyphicon
                            glyph="pencil"/>edit</Button></Col>
                    </Row>
                    <Row>
                        <Col xs={12}>
                            <div>
                                <ul className="list-unstyled">
                                    {this.getLinks()}
                                </ul>
                            </div>
                            <div>
                                {this.state.editmode && <NewLink onSubmitLink={this.handleNewLink}/>}
                            </div>
                        </Col>
                    </Row>
                </Well>)
        } else if (this.state.loading === true) {
            return <Well><img src={ripple} alt="loading..."></img></Well>
        } else {
            return <Well>Load failed!</Well>
        }
    }
}

LinkBox.propTypes = {
    linkUrl: React.PropTypes.string.isRequired,
    title: React.PropTypes.string
}
LinkBox.defaultProps = {
    title: ""
}
