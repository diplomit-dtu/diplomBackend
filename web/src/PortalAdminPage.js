/**
 * Created by Christian on 01-08-2017.
 */

import React, {Component} from 'react'
import PropTypes from 'prop-types';
import {Col, Grid, Row} from "react-bootstrap";
import Rip from './rest/Rip'
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table";
import CheckboxComp from "./components/CheckboxComp";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.min.css';

export default class PortalAdminPage extends Component {

    constructor(props){
        super(props);
        this.getUsers();
        this.state={
            users: []
        }
    }

    userUrl = this.props.apiUrl + '/users';
    getUsers = ()=>{
        Rip.getJson(this.userUrl, (json)=>{
            let users = json.forEach((user)=>{
                return this.checkUserRoles(user)
            })
            this.setState({users:json})
        })
    }

    checkUserRoles = (user)=>{
        user.isPortalAdmin=false; user.isCourseAdmin=false;
        if (user.roles){
            user.roles.forEach((role)=>{
                if(role.roleName==="PortalAdmin"){
                    user.isPortalAdmin=true;
                }
                if (role.roleName==="CourseAdmin"){
                    user.isCourseAdmin=true;
                }
            })
        }
        return user;
    }

    toggleCourseAdmin = (checked, id)=>{

        let url = this.userUrl + '/' + id + (checked ?  '/makeCourseAdmin' : '/removeCourseAdmin');
        Rip.post(url,{},
            (json)=>{
            console.log(json);
                this.updateUser(json);

        },
            (response)=>{
            toast.warning('Opdatering mislykkedes:' + response.message)
        })
    }

    togglePortalAdmin = (checked, id)=>{

        let url = this.userUrl + '/' + id + (checked ?  '/makePortalAdmin' : '/removePortalAdmin');
        Rip.post(url,{},
            (json)=>{
                console.log(json);
                toast.info('Updated user')
                this.updateUser(json);

            },
            (response)=>{
                console.log(response)
            })
    }

    updateUser = (newUser)=> {
        newUser = this.checkUserRoles(newUser);
        let users = this.state.users;
        users.forEach((user, index, array)=>{
            if (user.id ===newUser.id){
                array[index]=newUser;
            }
        })
        this.setState({users:users})


    }

    checkBoxFormatterPortalAdmin = (cell, row)=>{
        return <CheckboxComp checked={row.isPortalAdmin} onCheck={this.togglePortalAdmin} id={row.id}/>
    };

    checkBoxFormatterCourseAdmin = (cell, row)=>{
        return <CheckboxComp checked={row.isCourseAdmin} onCheck={this.toggleCourseAdmin} id={row.id}/>
    };
    booleanSortPortalAdmin = (a, b, order)=>{
        let orderInt = a.isPortalAdmin ? 1 : 0;
        orderInt -= b.isPortalAdmin ? 1: 0;
        return order==='desc' ? -orderInt : orderInt;
    };

    booleanSortCourseAdmin = (a,b,order)=>{
        let orderInt = a.isCourseAdmin ? 1 : 0;
        orderInt -= b.isCourseAdmin ? 1: 0;
        return order==='desc' ? -orderInt : orderInt;
    }
    render(){
        return <Grid >
            <ToastContainer position="bottom-right" autoClose={2000}/>
            <Row>
                <Col>
                    {/*<Panel header={<h3>Fremmøde</h3>}>*/}
                        {/*<Table responsive hover>*/}
                            {/*<thead>*/}
                            {/*<tr>*/}
                                {/*<th>Brugernavn</th>*/}
                                {/*<th>Navn</th>*/}
                                {/*<th>*/}
                                    {/*<OverlayTrigger placement={'top'} overlay={<Tooltip>Giver brugeren rettigheder til at oprette kurser</Tooltip>}>*/}
                                        {/*<div>Kursus Administrator</div>*/}
                                    {/*</OverlayTrigger>*/}
                                {/*</th>*/}
                                {/*<th><div><span>Portal Administrator</span></div></th>*/}
                            {/*</tr>*/}
                            {/*</thead>*/}
                            {/*<tbody>*/}
                            {/*<tr>*/}
                                {/*<td>s134000</td>*/}
                                {/*<td>Christian Budtz</td>*/}
                                {/*<td width="20px"><input id="lek1" type="checkbox" defaultChecked={true}/><label htmlFor="lek1"> </label></td>*/}
                                {/*<td width="20px"><input id="lek2" type="checkbox" defaultChecked={true}/><label htmlFor="lek2"> </label></td>*/}
                            {/*</tr>*/}
                            {/*</tbody>*/}
                        {/*</Table>*/}

                    {/*</Panel>*/}
                    <BootstrapTable data={this.state.users} pagination={true} options={{paginationPosition:'top',
                        sizePerPageList: [ {
                        text: '10', value: 10
                    }, {
                        text: '100', value: 100
                    }, {
                        text: 'All', value: this.state.users.length
                    } ]}}>
                        <TableHeaderColumn dataField='id' key={0} isKey={true} hidden={true} >Id</TableHeaderColumn>
                        <TableHeaderColumn dataField='userName' key={1} dataSort={true}>BrugerNavn</TableHeaderColumn>
                        <TableHeaderColumn dataField='firstName' key={1} dataSort={true}>Fornavn</TableHeaderColumn>
                        <TableHeaderColumn dataField='lastName' key={1} dataSort={true}>Efternavn</TableHeaderColumn>
                        <TableHeaderColumn dataField='email' key={1} dataSort={true}>Email</TableHeaderColumn>
                        <TableHeaderColumn dataField='isPortalAdmin' key={1} dataFormat={this.checkBoxFormatterPortalAdmin} dataSort={true} sortFunc={this.booleanSortPortalAdmin}>PortalAdmin</TableHeaderColumn>
                        <TableHeaderColumn dataField='isCourseAdmin' key={1} dataFormat={this.checkBoxFormatterCourseAdmin} dataSort={true} sortFunc={this.booleanSortCourseAdmin}>CourseAdmin</TableHeaderColumn>

                    </BootstrapTable>
                </Col>

            </Row>

        </Grid>

    }
}

PortalAdminPage.propTypes={
    apiUrl: PropTypes.string
}