/**
 * Created by Christian on 07-04-2017.
 */
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Row, Col, Grid, Button, FormGroup, ControlLabel, FormControl, Form, Label} from 'react-bootstrap';
import {observer} from "mobx-react";

class ProfilePage extends Component {
    deletedb = ()=>{
        const answer = window.confirm("Er du sikker på at du vil slette din database?!! Alle data vil gå tabt");
        if (answer){
            this.props.store.deletedb();
        }
    };
    createDB = ()=>{
        this.props.store.createDB();
    };
    updatePass = ()=> {
        this.props.store.updatePass();
    };

    constructor(props){
        super();
        this.state={
            firstName:props.user.firstName,
            lastName:props.user.lastName,
            email:props.user.email
        }



    }
    updateUserBtnPressed = ()=>{
        let updatedUser= this.props.user;
        updatedUser.firstName = this.state.firstName;
        updatedUser.lastName = this.state.lastName;
        updatedUser.email = this.state.email;
        this.props.updateUser(updatedUser);
    };
    resetForm = ()=>{
        this.setState({
            firstName:this.props.user.firstName,
            lastName:this.props.user.lastName,
            email:this.props.user.email
        })
    };
    render(){
        let dbInfo = this.props.store.dbInfo;
        return <Grid>
            <Row>
                <Col xsOffset={3} xs={6} className="text-center">
                    <Form >
                        <FormGroup>
                            <ControlLabel>Fornavn</ControlLabel>
                            <FormControl type="text" value={this.state.firstName} onChange={(e)=>this.setState({firstName:e.target.value})}>
                            </FormControl>
                        </FormGroup>
                        <FormGroup>
                            <ControlLabel>Efternavn</ControlLabel>
                            <FormControl type="text" value={this.state.lastName} onChange={(e)=>this.setState({lastName:e.target.value})}>
                            </FormControl>
                        </FormGroup>
                        <FormGroup>
                            <ControlLabel>Email</ControlLabel>
                            <FormControl type="text" value={this.state.email} onChange={(e)=>this.setState({email:e.target.value})}>
                            </FormControl>
                        </FormGroup>
                    </Form>
                    <Button onClick={this.updateUserBtnPressed}>Opdater Bruger</Button>
                    <Button onClick={this.resetForm}>Reset</Button>
                    <h4>Din personlige Database</h4>
                    {!this.props.store.loading && dbInfo.id &&
                    <div>
                        <Label>Host</Label><div>{dbInfo.hostUrl}</div>
                        <Label>BrugerNavn</Label><div>{dbInfo.id}</div>
                        <Label>Kodeord</Label><div>{dbInfo.pass}</div>
                        <Label>Database navn</Label><div>{dbInfo.id}</div>
                        <Label>Skriverettigheder</Label><div>{dbInfo.revoked ? "Nej - reducer din database størrelse til under 50 MB" :"Ja - Maks database størrelse er 50 MB"}</div>
                        <Label>Databasestørrelse</Label><div>{dbInfo.size} MB</div>
                        <Button onClick={this.updatePass}>Nyt kodeord</Button>
                        <Button bsStyle={"danger"} onClick={this.deletedb}>Slet din database</Button>
                    </div>

                    }
                    {!this.props.store.loading && !dbInfo.id &&
                    <div>
                        <Button onClick={this.createDB}>Opret ny database</Button>
                    </div>
                    }
                    {this.props.store.loading &&
                        <div>Opdaterer database...</div>}

                </Col>
            </Row>
        </Grid>
    }
}

ProfilePage.propTypes = {
    updateUser: PropTypes.func,
    user: PropTypes.shape({
        firstName:PropTypes.string,
        lastName:PropTypes.string,
        email:PropTypes.string
    })
}

export default observer(ProfilePage)