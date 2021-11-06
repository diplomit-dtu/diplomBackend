/**
 * Created by Christian on 31-07-2017.
 */
import React, {Component } from 'react';
import PropTypes from 'prop-types';
import {Button, Glyphicon, Grid, Well} from "react-bootstrap";
import ContentEditable from "react-contenteditable";
import {styles} from "./index";

export default class CourseInfoPage extends Component{

    constructor(props) {
        super(props);
        let content = props.content;
        if (content===null || content===undefined)
            content=[];
        content = JSON.parse(JSON.stringify(content)); //Deep clone - most efficient way
        this.state = {
            content: content
        }
    }
    updateLine = (event,index)=>{
        console.log(event.target.value);
        console.log(index);
        let newContents = this.state.content;
        newContents[index] = {title:this.state.content[index].title, content:event.target.value};
        console.log(newContents);
        this.setState({content: newContents});
        // this.props.updateCourseInfo(newContents)
    }


    updateTitle = (event, index) =>{
        let newContents = this.state.content;
        newContents[index] = {title:event.target.value, content: this.state.content[index].content};
        this.setState({content:newContents});
    }

    addLine = ()=>{
        let content = this.state.content;
        content.push({title:"titel", content:"indhold"})
        this.setState({conent:content})
    };


    deleteLine =(index)=> {
        let content = this.state.content;
        console.log("index" + index);
        console.log(content);
        content.splice(index,1);
        console.log(content);
        this.setState({content:content});

    }

    cancelChange = ()=>{
        this.setState({content:JSON.parse(JSON.stringify(this.props.content))})
    };

    saveLines = ()=>{
        this.props.saveContent(this.state.content);
    };

    isAdmin = ()=>{
        if (this.props.user && this.props.course && this.props.course.admins) {
            return this.props.course.admins.includes(this.props.user.id)
        }else {
            return false;
        }
    }

    generateEditableList = ()=>{

        var content =  (<dl className="dl-horizontal">
            {this.state.content.map((info, index)=>{
                return <span key={index}>
                    <dt><div style={styles.a} onClick={()=>this.deleteLine(index)}><Glyphicon glyph="remove" style={{float:"left"}}/></div><ContentEditable style={{float:"right"}}id={index} html={info.title} onChange={(event) => this.updateTitle(event, index)}/></dt>
                    <dd><ContentEditable id={index} html={info.content} onChange={(event)=>this.updateLine(event,index)}/> </dd>
                </span>
            })
            }
            <div style={styles.a}> <Glyphicon glyph="plus"/></div>
        </dl>)
        return content;


    }
    generateList = ()=>{

        var content =  (<dl className="dl-horizontal">
            {this.state.content.map((info, index)=>{
                return <span key={index}>
                    <dt>{info.title}</dt>
                    <dd dangerouslySetInnerHTML={{__html:info.content}}/>
                </span>
            })
            }
        </dl>)
        return content;


    }

    render(){
        return <Grid>
            <Well>
                <h4>Kursusinformation</h4>
                <hr/>
                {this.isAdmin() ?
                    this.generateEditableList() :
                    this.generateList()}
                {this.isAdmin() &&<span>
                <Button bsStyle={"primary"} onClick={this.saveLines}>Gem
                </Button>
                    < Button onClick={this.cancelChange}>Annullér</Button>
                </span>
                }
            </Well>
        </Grid>

    }

}
CourseInfoPage.propTypes = {
    content: PropTypes.arrayOf(PropTypes.shape({
            title: PropTypes.string,
            content: PropTypes.string,
            saveContent: PropTypes.func,
            user: PropTypes.any
        }
    ))
}