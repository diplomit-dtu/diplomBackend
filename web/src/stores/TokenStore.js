import {decorate, observable, action, reaction, computed} from 'mobx';
import {toast} from 'react-toastify';

class TokenStore {
    token = window.localStorage.getItem('jwt');

    constructor(){
        reaction(
            ()=>this.token,
            token => {
                if (token){
                    window.localStorage.setItem('jwt', token);
                } else {
                    window.localStorage.removeItem('jwt');
                }
            })
        this.startAutoCheck();
    }

    setToken(token){
        this.token = token;
        this.startAutoCheck();
    }

    get user(){
        if (!this.token) return null;
        return this.decodeClaims(this.token).user;
    }

    get sampleViewer(){
        return this.checkUserHasOneOfRoles(["Læseprøve", "Administrator"]);
    }

    get fullViewer(){
        return this.checkUserHasOneOfRoles(["Fuldtekst","Administrator"]);
    }

    get admin(){
        return this.checkUserHasOneOfRoles(["Administrator"]);
    }

    checkUserHasOneOfRoles = (roles)=>{
        let hasRole = false;
        if (this.token && this.getClaims().user && this.getClaims().user.roles) {
            roles.forEach((role) => {
                this.getClaims().user.roles.forEach((userRole) => {
                    if (role === userRole.roleName) hasRole = true;
                })
            })
        }
        return hasRole;
    };

    getClaims = ()=>{
        return this.decodeClaims(this.token);
    };

    decodeClaims = (token) => {
        if (!token) return null;
        const claims = token.split(".")[1];
        //const decodedClams = decodeURIComponent(encodeURI(window.atob(claims)));
        const decodedClams = decodeURIComponent(escape(window.atob(claims))); //FIXME temp fix with encoding issue
        return JSON.parse(decodedClams);
    };

    logout(exp) {
        this.token = null;
        clearTimeout(this.timer);
        if (exp) {
            debugger;
            toast.warn("Dit login er udløbet. Log venligst ind igen")
        }
    }

    startAutoCheck() {
        if (!this.token) return;
        const exp = new Date(this.getClaims().exp * 1000);
        const now = new Date();
        const timeOut = (exp - now) - 10000;
        this.timer = setTimeout(()=>
                this.logout(true)
            , timeOut);
    }
}
decorate(TokenStore, {
    token: observable,
    setToken: action,
    user: computed,
    sampleViewer:computed,
    fullViewer:computed,
    admin:computed,
    logout: action,
    updateSelf:action

});

export default TokenStore;