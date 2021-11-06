import {decorate, observable, reaction, action, autorun} from 'mobx';
import TokenStore from "./TokenStore";
import Rip from "../rest/Rip";


class ProfileStore {
    user = null;
    dbInfo = {id: null, pass: null, revoked:null,hostUrl:""};
    tokenStore = {user:""};
    apiUrL = "";
    loading = "false";

    constructor(api){
        this.apiUrL=api;
        this.fetchDb();
        reaction(()=> this.tokenStore.user
            ,   this.fetchDb
        )

    }

    createDB = ()=>{
        if (this.tokenStore.user){
            console.log("Creating own db");
            this.loading = true;
            Rip.createOwnDb(this.apiUrL,this.tokenStore.user.userName,
                (json)=>{
                    this.dbInfo = json;
                    this.loading = false;
                    debugger;
                },
                ()=>{
                this.loading = false;
                    this.fetchDb()
                }
            )
        }
    }

    updatePass = ()=>{
        this.loading = true;
        Rip.updateOwnPass(this.apiUrL, this.tokenStore.user.userName,
            (json)=>{
            this.dbInfo = json;
            this.loading = false;
            },
            ()=>{
            this.loading = false;
            this.fetchDb();
            }
            )
    }

    fetchDb=() =>{
        this.loading = true;
        if (this.tokenStore.user) {
            console.log("Fetching Own DB" );
            Rip.getOwnDb(this.apiUrL, this.tokenStore.user.userName, (json)=>{
                this.dbInfo=json;
                this.loading = false;
            }, ()=>{
                this.dbInfo= {};
                this.loading = false;
            });
        }
    }

    deletedb = ()=>{
        if (this.tokenStore.user){
            console.log("Deleting own DB");
            this.loading  = true;
            Rip.deleteOwnDb(this.apiUrL,this.tokenStore.user.userName,
                ()=>{
                    this.loading = false;
                    this.fetchDb();
                },
                ()=>{
                    this.loading = false;
                    this.fetchDb()
                })
        }

    }
}

decorate(ProfileStore,{
    user:observable,
    dbInfo:observable,
    tokenStore: observable,
    loading: observable,
    createDb: action,
    fetchDb: action,
    deletedb: action

});

export default  ProfileStore;