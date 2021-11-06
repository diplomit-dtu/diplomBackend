/**
 * Created by Christian on 02-05-2017.
 */
import React, {Component} from "react";
import PropTypes from 'prop-types';
import {
    Button,
    Col,
    ControlLabel,
    Form,
    FormControl,
    FormGroup,
    Glyphicon,
    MenuItem,
    Modal,
    Nav,
    Navbar,
    NavDropdown,
    NavItem
} from "react-bootstrap";
import Config from "./config";
import {arrayMove, SortableContainer, SortableElement, SortableHandle} from "react-sortable-hoc";
import {styles} from "./index";

export default class TopMenu extends Component {
    sortEnd = ({oldIndex, newIndex})=>{
        console.log("Move from:" + oldIndex + " to: " + newIndex)
        this.props.onLinksUpdated(arrayMove(this.props.links,oldIndex,newIndex))

    };
    handlenewLinkClicked = ()=>{
        this.setState({showModal:true})
    };
    handlesubmitLink = (e)=>{
        e.preventDefault();
        let links = this.props.links;
        links.push({text:this.state.textInput,href:this.state.hrefInput})
        this.setState({showModal:false, textInput:'', hrefInput:'http://'})
        this.props.onLinksUpdated(links)
    };
    handleLinkRemove = (e)=>{
        console.log(e);
        let links = this.props.links;
        links = links.filter((link)=>{
            console.log(link)
            return !(link.text ===e.text && link.href===e.href)
        })
        this.props.onLinksUpdated(links);
    };

    constructor(props) {
        super(props);
        this.state = {active: props.activeId, showModal:false, hrefInput:'http://',textInput:''}
    };

    handleAvatarClick = (eventKey) => {

        if(this.props.avatar.id){
            if(eventKey===0.1){
                this.props.onLogout();
            } else {
                console.log("editing profile")
                this.props.onProfileEdit();
            }
        } else {
            //Send User to CampusnetLogin
            const redirectUrl = Config.ApiPath ? Config.ApiPath + Config.campusNetServiceUrl : Config.campusNetServiceUrl;
            window.location.replace(redirectUrl);
        }
    };


    handleNavSelect = (eventKey) => {
        this.setState({active: eventKey})
        console.log("TopMenu Selection:" )
        console.log(eventKey)
        this.props.onSelect(eventKey);
    };

    getUserMenu = (e)=> {
        if (this.props.avatar.id){
            return <NavDropdown id="avatarDropDown" eventKey={0} title={this.props.avatar.id}>
                <NavItem id="avatarLogout" eventKey={0.1}>Logout</NavItem>
                <NavItem id="editProfile" eventKey={0.2}>Profil</NavItem>
            </NavDropdown>
        } else {
            return <NavItem>
                Login
            </NavItem>
        }
    }

    getNavContent = () => {
        return this.props.menuItems.map((nav, no) => {
            if (nav.type === "NavText") {
                return <NavItem key={nav.id} eventKey={nav.id}/>
            } else if (nav.type === "NavItem") {
                return <NavItem key={no} eventKey={nav.id}
                                active={this.state.active.component === nav.id.component}>{nav.text}</ NavItem>
            } else if (nav.type === "NavDropDown") {

                nav.items = nav.items.sort(function(a, b){
                    if(a.text < b.text) { return -1; }
                    if(a.text > b.text) { return 1; }
                    return 0;});
                const items = nav.items.map((item, no) => {
                    return (<MenuItem key={no} eventKey={item.id}>{item.text}</MenuItem>)
                });
                return (<NavDropdown key={nav.id} id={nav.id} title={nav.text}>
                    {items}
                </NavDropdown>)
            } else if (nav.type === "Avatar") {
                return (<NavItem pullRight={true}>Avatar</NavItem>)
            } else {
                return
            }
        });
    }


    getLinkContent =()=> {
        if (!(this.props.links)) return;
        return this.props.links.map((link)=>{
            return <NavItem style={styles.a} key={link.href} onClick={(e)=>{e.preventDefault(); window.open(link.href)}}>{link.text}</NavItem>
        })

    }


    isAdmin = ()=>{
        if (this.props.user && this.props.course && this.props.course.admins) {
            return this.props.course.admins.includes(this.props.user.id)

        }else {
            return false;
        }
    }


    render() {
        console.log(this.isAdmin())
        let isLoggedIn = !!this.props.user;
        return <div className="NavbarContainer">

            <Navbar className="navbar-fixed-top" fluid>
                <Navbar.Header>
                    <Navbar.Toggle/>
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav onSelect={this.handleNavSelect}>
                        {this.getNavContent()}

                        {isLoggedIn &&(this.isAdmin() ?
                            <SortableNav helperClass="sortableHelper" useWindowAsScrollContainer={true} useDragHandle={true}
                                         items={this.props.links ? this.props.links : []} onSortEnd={this.sortEnd}
                                         newLink={this.handlenewLinkClicked}
                                         removeLink={this.handleLinkRemove}
                            />
                            :
                            <NavDropdown id="Links" title="Links">
                                {this.getLinkContent()}

                            </NavDropdown>
                        )}


                    </Nav>

                    <Nav onSelect={this.handleAvatarClick} pullRight>
                        {this.getUserMenu()}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
            <Modal bsSize={"sm"} show={this.state.showModal} onHide={()=>this.setState({showModal:false})}>
                <Modal.Header closeButton><h4>Nyt Link</h4></Modal.Header>
                <Modal.Body>

                    <Form horizontal>
                        <FormGroup controlId="formHorizontalEmail">
                            <Col componentClass={ControlLabel} sm={4}>
                                Tekst
                            </Col>
                            <Col sm={6}>
                                <FormControl type="text" placeholder="tekst" value={this.state.textInput} onChange={(e)=>this.setState({textInput:e.target.value})}/>
                            </Col>
                        </FormGroup>

                        <FormGroup controlId="formHorizontalPassword">
                            <Col componentClass={ControlLabel} sm={4}>
                                Url
                            </Col>
                            <Col sm={6}>
                                <FormControl type="url" placeholder="http://" value={this.state.hrefInput} onChange={(e)=>this.setState({hrefInput:e.target.value})}/>
                            </Col>
                        </FormGroup>

                        <FormGroup>
                            <Col smOffset={2} sm={10}>
                                <Button type="submit" onClick={this.handlesubmitLink}>
                                    Gem link
                                </Button>
                            </Col>
                        </FormGroup>
                    </Form>
                </Modal.Body>
            </Modal>
        </div>
    }

}

TopMenu.propTypes = {
    onLogout: PropTypes.func,
    onProfileEdit: PropTypes.func,
    onSelect: PropTypes.func,
    onLinksUpdated: PropTypes.func,
    menuItems: PropTypes.array,
    user: PropTypes.any,
    course: PropTypes.any
}

const DragHandle = SortableHandle(()=> <span><Glyphicon glyph="resize-vertical"/></span>)


const SortableItem = SortableElement(({value, removeLink}) =>
    <NavItem href={value.href} onClick={(e)=>{e.preventDefault();window.open(value.href)}}><DragHandle/>{value.text} <Glyphicon value={value.href} glyph="remove" onClick={(e)=>{e.stopPropagation();removeLink(value)}}/></NavItem>
);

const SortableNav = SortableContainer(({items, newLink, removeLink}) => {
    return (

        <NavDropdown title="Links" id="Links">
            {items.map((value, index) => (
                <SortableItem key={value.href} index={index} value={value} removeLink={(e)=>removeLink(e)} />
            ))}
            <NavItem onClick={newLink}><Glyphicon glyph="plus"/> nyt link</NavItem>

        </NavDropdown>
    );
});