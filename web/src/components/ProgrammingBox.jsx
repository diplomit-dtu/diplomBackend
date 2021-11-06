/**
 * Created by Christian on 23-05-2017.
 */
import React, {Component} from "react";
import PropTypes from 'prop-types';

import {Button, ButtonGroup, Panel, PanelGroup} from "react-bootstrap";
import update from 'immutability-helper';

export default class ProgrammingBox extends Component {

    constructor(props) {
        super(props);
        this.state = {
            mainClass: "Main",
            files: [
                {
                    "fileName": "Main.java",
                    "fileContents": props.code
                }],
            input: [],
            activeFile: 0,
            systemOut: [],
            systemErr: [],
            compiling: false

        }


    }

    handleCodeChange = (e) => {
        const newContents = e.target.value
        const activeIndex = this.state.activeFile;
        const newState = update(this.state,{files:{[activeIndex]:{fileContents:{$set : newContents}}}});
        this.setState(newState);

    }

    handleAceEditorChange = (e) =>{
        const activeIndex = this.state.activeFile;
        const newState = update(this.state,{files:{[activeIndex]:{fileContents:{$set : e}}}});
        this.setState(newState);
    }
    handleRunClick = (e) =>{
        const requestObj = {mainClass:this.state.mainClass,
            files:this.state.files,
            input:this.state.input}
        const requestJSON = JSON.stringify(requestObj);
        var runState = update(this.state,{
            compiling:{$set:true},
            systemOut:{$set:[]},
            systemErr:{$set:[]}
        })
        this.setState(runState)
        fetch("https://javacompile.herokuapp.com",{
            method: 'POST',
            mode: 'cors',
            body: requestJSON,
            headers: new Headers({
                'Content-type': 'application/json'
            })
        }).then((response)=>{
            response.json().then((json)=>{
                console.log(json);
                var runRes = json.runResult;


                if (!runRes) {
                    runRes = {systemOut:["Compilation Failed"],
                        systemErr:["Compile Error"]}
                }
                var newState = update(this.state,{
                    compiling:{$set:false},
                    systemOut:{$set:runRes.systemOut},
                    systemErr:{$set:runRes.systemErr}}
                );
                this.setState(newState);
            })
        })
    }

    getSystemOut = ()=>{
        return this.mapLines(this.state.systemOut);
    }
    getSystemErr = ()=>{
        return this.mapLines(this.state.systemErr);
    }

    mapLines = (array) => {
        if (array==null) return ('');
        return array.map((line, key) => {
            return <span key={key}>{line}<br/></span>
        })
    }


    render() {
        return (
            <div>
                <h4>
                    Kode-Editor
                </h4>

                {/*<AceEditor*/}
                {/*    markers={[{*/}
                {/*        startRow:1,startCol:1,endRow:1,endCol:1,type:"Error"}*/}
                {/*    ]}*/}
                {/*    mode="java"*/}
                {/*    theme="eclipse"*/}
                {/*    name="testBox"*/}
                {/*    height="15em"*/}
                {/*    setOptions={{*/}
                {/*        enableBasicAutocompletion: true,*/}
                {/*        enableLiveAutocompletion: true,*/}
                {/*        enableSnippets: true,*/}
                {/*        showLineNumbers: true,*/}
                {/*        tabSize: 10,*/}
                {/*    }}*/}
                {/*    value={this.state.files[this.state.activeFile].fileContents}*/}
                {/*    onChange={this.handleAceEditorChange}*/}


                {/*/>*/}

                <ButtonGroup>
                    <Button onClick={this.handleRunClick} className={this.state.compiling ? "disabled" : ""}>
                        {this.state.compiling ? "Running Code...":"Run Code!"}
                    </Button>
                </ButtonGroup>
                <PanelGroup>
                    <Panel header="System.Out" style={{height:200, overflowY:'scroll',fontFamily:"monospace"}}>{this.getSystemOut()}</Panel>
                    <Panel header="System.Err" style={{height:200, overflowY:'scroll', color:"red", fontFamily:"monospace"}}>{this.getSystemErr()}</Panel>

                </PanelGroup>
            </div>)

    }

}

ProgrammingBox.propTypes={
    code:PropTypes.string.isRequired
}

