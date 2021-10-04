import { LocalStoreService } from './../../@core/services/local-store.service';
import {Component, OnInit} from '@angular/core';
import {AppConst} from "../../@core/utils/app-const";
import {Router} from "@angular/router";
import {LoginService} from "../../@core/services/login.service";
import {FormGroup} from "@angular/forms";
import {JwtAuthService} from "../../@core/services/jwt-auth.service";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../../../environments/environment";
import {User} from "../../@core/models/user";
import {HelperService} from "../../@core/services/helper.service";
import { Subject } from 'rxjs';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  serverPath = AppConst.SERVER_PATH;
  errorMsg;
  credential = {'username': '', 'password': ''};
  REF : string;
  test = '%2Fhome%3FRefID%3D3C6EE6BB683E147E72DAA9C026600B435ED7BE0F22';
  log;
  user = {
    "partnerEntityID": "pfTest",
    "instanceId": "mfaRef",
    "mail": "test@example.com",
    "subject": "test",
    "authnCtx": "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified",
    "sessionid": "PgQagrYFnqYxo5v239ALAAmWgzO",
    "authnInst": "2021-09-29 14:37:47-0400"
  };
  Admin : boolean;
  noAdmin : boolean;
  jwt = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
  jwtOut;

  constructor(
    private ls:LocalStoreService,
    private loginService: LoginService,
    private router: Router,
    private jwtAuth: JwtAuthService,
    private http: HttpClient,
    private helperService: HelperService
  ) {
  }

  onLogin() {
    this.jwtAuth.signin(this.credential.username, this.credential.password)
      .subscribe(res => {
        this.jwtAuth.setToken(res['result'].token);
        const httpOptions = {
          headers: new HttpHeaders({'Authorization': 'Bearer ' + res.result.token})
        };

        this.http.get(`${environment.apiURL}/user/getCurrentUser`, httpOptions).subscribe(
          user => {
            const me = <User> user;

            // me.roles = [];
            // for (let i = 0; i < authorities.length; i++) {
            //   const role = authorities[i].authority;
            //   me.roles.push(role);
            // }
            console.log(me);
            this.jwtAuth.setUserAndToken(res.result.token, me, !!res);

            this.router.navigateByUrl(this.jwtAuth.return);
          }, error => {
          }
        );
      }, err => {
        this.errorMsg = 'Invalid Username/Password. Please try again.';
      })
  }

testAdm(){
  // console.log('calling be acs');
  // this.http.get(`http://localhost:8181/ssportal/be/saml-acs?REF=909090`).subscribe(
  //   res => {
  //     console.log(res);
  //   }
  // )

  //redirect
  //window.location.href='http://localhost:8181/ssportal/be/saml-acs?REF=909090'
  this.router.navigate(['/services'])
}

testNoAdmin(){
  this.router.navigate(['/home'])
}

parseJwt () {
  var base64Url = this.jwt.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));
  this.ls.setItem('SSPORTAL_APP_USER_FROM_JWT', jsonPayload)
  console.log(jsonPayload)
  this.jwtOut = JSON.parse(jsonPayload);
  this.ls.setItem('SSPORTAL_APP_USER_FROM_JWT2', this.jwtOut)
  console.log(this.jwtOut)
};


  ngOnInit(): void {
    this.REF = this.test.substring(this.test.length - 42);
    console.log(this.REF);
    this.http.get(`http://localhost:8080/sso/authenticate?REF=${this.REF}`).subscribe(
      res => { 
           console.log(res);
     }
    )
    // *ngif='Admin' html

     this.ls.setItem('SSPORTAL_APP_USER', this.user)
    if (this.user.subject == 'testAdm') {
      this.Admin=true;
    //this.router.navigate(['/services']);
    }else{
      this.noAdmin=true;
    //this.router.navigate(['/home']);
    }
  }
}