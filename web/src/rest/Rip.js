/**
 * Created by Christian on 29-05-2017.
 */
import 'whatwg-fetch';
import JwtHandler from '../jwthandler'

const debug = true;
export default class Rip {

    static getJson = (url, callback, catchback) => {
        if (debug) console.log("Rip: Fetching from " + url);
        const jwtToken = JwtHandler.getToken();
        fetch(url, {
            mode: 'cors',
            method: 'GET',
            headers: new Headers({
                "content-type": "application/json",
                "authorization" : "Bearer " + jwtToken
            })
        }).then((response) => {
            if(response.status!==200){
                catchback(response)
            } else {
                Rip.handleJSON(response, callback, catchback);
            }
        }).catch((response) => {
            catchback({message: "Rip: Error while fetching: " + response.message, response: response});
        })
    }

    static getPlain = (url, callback,catchback) => {
        fetch(url,{
            mode: 'cors',
            method: 'GET'
        }).then((response)=>{
            console.log('got response');
            response.text().then((text) =>{
                if(response.status!==200 && response.status!==204 && response.status!==201){
                    catchback({message:"Rip: Error while GET'ing: " + text, response: response})
                }
                callback(text);
            })
        }).catch((response)=>{
            catchback({message:"Rip: Error while GET'ing " + response.message, response: response});
        })
    }

    static getNoCors = (url) =>{
        fetch(url,{
            mode: 'no-cors',
            method: 'GET'
        })
    }

    static post = (url, json, callback, catchback) => {
        const token = JwtHandler.getToken();
        fetch(url, {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(json),
            headers: new Headers({
                'Content-type' : 'application/json',
                'authorization' : 'Bearer ' + token
            })
        }).then((response) => {
            if (response.status!==200 && response.status!==201 && response.status!==204){
                if (response.status===403 ){
                    response.text().then((text)=>{
                        if (text==="Token too old!"){
                            alert("Dit login er udløbet - Du må logge ind igen")
                        }
                    })
                }
                catchback({message:"Rip: Error while POST'ing: " + response.message, response: response})
            }
            Rip.handleJSON(response,callback,catchback);
        }).catch((response) => {
            catchback({message: "Rip: Error while POST'ing: " + response.message, response: response});
        })
    }

    static put = (url, json, callback, catchback) => {
        const token = JwtHandler.getToken();
        fetch(url, {
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(json),
            headers: new Headers({
                'Content-type' : 'application/json',
                'authorization' : 'Bearer ' + token
            })
        }).then((response) => {
            console.log(response.status)
            if (response.status!==200 && response.status!==201 && response.status!==204){
                catchback({message:"Rip: Error while PUT'ing: " + response.message, response: response})
            }
            Rip.handleJSON(response,callback,catchback);
        }).catch((response) => {
            console.log(response)
            catchback({message: "Rip: Error while PUT'ing: " + response.message, response: response});
        })
    }

    static postForString = (url,json,callback, catchback)=>{
        const token = JwtHandler.getToken();
        fetch(url, {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(json),
            headers: new Headers({
                'Content-type' : 'application/json',
                'authorization' : 'Bearer ' + token
            })
        }).then((response) => {
            if(response.status === 200 || response.status === 204 || response.status ===201) {
                callback({message: "Rip: OK while POST'ing: " + response.message, response: response});
            } else {
                catchback({message: "Rip: Error while POST'ing: " + response.message, response: response});
            }
        }).catch((response) => {
            catchback({message: "Rip: Error while POST'ing: " + response.message, response: response});
        })
    }

    static del = (url,callback, catchback) =>{
        const token = JwtHandler.getToken();
        fetch(url, {
            mode: 'cors',
            method: 'DELETE',
            headers: new Headers({
                'Content-type' : 'application/json',
                'authorization' : 'Bearer ' + token
            })
        }).then((response) => {
            if(response.status === 200 || response.status === 204 || response.status ===201) {
                callback({message: "Rip: OK DELETING: " + response.message, response: response});
            } else {
                catchback({message: "Rip: Error while DELETING'ing: " + response.message, response: response});
            }
        }).catch((response) => {
            catchback({message: "Rip: Error while POST'ing: " + response.message, response: response});
        })

    }

    static handleJSON = (response,callback,catchback)=>{
        if (!response.ok) {
            const ripError = {message: "Rip: Response not ok " + response.error, response: response}
            if (debug) console.log(ripError);
            catchback(ripError);
        }
        response.json().then((json) => {
            callback(json);
        }).catch((error) => {
            const riperror = {message: "Rip: error while parsing json: " + error, response: response};
            if (debug) console.log(riperror);
            catchback(riperror);
        })
    }

    static getOwnDb = (apiURL, userName,callback, catchback) =>{
        Rip.getJson(apiURL + "/database/self",(json)=>{
            callback(json);
        }, catchback)

    }

    static deleteOwnDb(apiUrL, userName, callback, catchback) {
        Rip.del(apiUrL + "/database/self", callback, catchback
        )
    }

    static createOwnDb(apiUrL, userName, callback, catchback) {
        Rip.post(apiUrL + "/database/self", callback , catchback)
    }

    static updateOwnPass(apiUrL, userName, callback, catchback) {
        Rip.post(apiUrL + "/database/self/pass",callback,catchback)
    }
}