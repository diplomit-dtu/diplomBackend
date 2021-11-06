/**
 * Created by Christian on 07-04-2017.
 */
import React, {Component} from 'react';
import {Row, Col, Grid, Button} from 'react-bootstrap';
import Config from './config'

class LoginPage extends Component {


    constructor(props){
        super(props);
        this.state = {
            
        }

    }

    cnLogin = () =>{
        const redirectUrl = Config.ApiPath ? Config.ApiPath + Config.campusNetServiceUrl : Config.campusNetServiceUrl;
        window.location.replace(redirectUrl);
    }
    render(){
        return <Grid>
            <Row>
                <Col xsOffset={3} xs={6} className="text-center">
                    <Button bsStyle="primary" onClick={this.cnLogin}> Login with campusNet</Button>
                </Col>
            </Row>
        </Grid>
    }
}
export default LoginPage;