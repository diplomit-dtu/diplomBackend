import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import 'bootstrap/dist/css/bootstrap.css';
//import 'bootstrap/dist/css/bootstrap-theme.css';
import './index.css';
import Config from './config';
import TokenStore from "./stores/TokenStore";
import ProfileStore from "./stores/ProfileStore";

export const styles = {a:{cursor:"pointer", color:"#337ab7"}}

//Some code to extract a potential token....
const token = getParameterByName("token");
console.log(token);
if (token!=null && token.length>0){
    //Store token and redirect to baseURL
    localStorage.setItem("portal-jwt-Token",token);
    window.location.replace("/");
}
// Base React class

const api =  Config.ApiPath ? Config.ApiPath + "/rest" : "/rest"

const stores = {
    TokenStore : new TokenStore(),
    ProfileStore: new ProfileStore(api)
}
stores.ProfileStore.tokenStore =stores.TokenStore
ReactDOM.render(
    //set apiUrl for deployment with seperate api host name eg: https://diplomportal.herokuapp.com/rest
  <App stores={stores} apiUrl={api} name="DiplomPortal"/>,
  document.getElementById('root')
);

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    //name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}