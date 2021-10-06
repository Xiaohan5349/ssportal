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
  // user = {
  //   "partnerEntityID": "pfTest",
  //   "instanceId": "mfaRef",
  //   "mail": "test@example.com",
  //   "subject": "test",
  //   "authnCtx": "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified",
  //   "sessionid": "123456",
  //   "authnInst": "2021-09-29 14:37:47-0400",
  //   "admin": "false"
  // };
  // userAdmin = {
  //   "partnerEntityID": "pfTest",
  //   "instanceId": "mfaRef",
  //   "mail": "testAdmin@example.com",
  //   "subject": "testAdmin",
  //   "authnCtx": "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified",
  //   "sessionid": "654321",
  //   "authnInst": "2021-09-29 14:37:47-0400",
  //   "admin": "true"
  // } 
  Admin : boolean;
  noAdmin : boolean;
  jwtAdmin = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXJ0bmVyRW50aXR5SUQiOiJwZlRlc3QiLCJpbnN0YW5jZUlkIjoibWZhUmVmIiwibWFpbCI6InRlc3RBZG1pbkBleGFtcGxlLmNvbSIsInN1YmplY3QiOiJ0ZXN0QWRtaW4iLCJhdXRobkN0eCI6InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOnVuc3BlY2lmaWVkIiwic2Vzc2lvbmlkIjoiNjU0MzIxIiwiYXV0aG5JbnN0IjoiMjAyMS0wOS0yOSAxNDozNzo0Ny0wNDAwIiwiYWRtaW4iOiJ0cnVlIn0K.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
  jwtUser = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXJ0bmVyRW50aXR5SUQiOiJwZlRlc3QiLCJpbnN0YW5jZUlkIjoibWZhUmVmIiwibWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJzdWJqZWN0IjoidGVzdCIsImF1dGhuQ3R4IjoidXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFjOmNsYXNzZXM6dW5zcGVjaWZpZWQiLCJzZXNzaW9uaWQiOiIxMjM0NTYiLCJhdXRobkluc3QiOiIyMDIxLTA5LTI5IDE0OjM3OjQ3LTA0MDAiLCJhZG1pbiI6ImZhbHNlIn0K.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
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
  this.ls.setItem('SSPORTAL_JWT_TOKEN', this.jwtAdmin);
  this.jwtAuth.parseJwt(this.jwtAdmin);
  this.jwtOut = this.ls.getItem('SSPORTAL_APP_USER');
  console.log(this.jwtOut);
  this.ls.setItem('SSPORTAL_APP_ADMIN',this.jwtOut.admin);
  this.router.navigate(['/services']);
}

testNoAdmin(){
  this.ls.setItem('SSPORTAL_JWT_TOKEN', this.jwtUser);
  this.jwtAuth.parseJwt(this.jwtUser);
  this.jwtOut = this.ls.getItem('SSPORTAL_APP_USER');
  this.ls.setItem('SSPORTAL_APP_ADMIN',this.jwtOut.admin);
  this.router.navigate(['/home']);
}

// parseJwt () {
//   var base64Url = this.jwt.split('.')[1];
//   var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
//   var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
//       return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
//   }).join(''));
//   this.ls.setItem('SSPORTAL_APP_USER_FROM_JWT', jsonPayload)
//   console.log(jsonPayload)
//   this.jwtOut = JSON.parse(jsonPayload);
//   this.ls.setItem('SSPORTAL_APP_USER_FROM_JWT2', this.jwtOut)
//   console.log(this.jwtOut)
// };


  ngOnInit(): void {
    this.REF = this.test.substring(this.test.length - 42);
    console.log(this.REF);
    this.http.get(`http://localhost:8080/sso/authenticate?REF=${this.REF}`).subscribe(
      res => { 
           console.log(res);
     }
    )
    // *ngif='Admin' html

    //  this.ls.setItem('SSPORTAL_APP_USER', this.user)
    // if (this.user.subject == 'testAdm') {
    //   this.Admin=true;
    // //this.router.navigate(['/services']);
    // }else{
    //   this.noAdmin=true;
    // //this.router.navigate(['/home']);
    // }
  }
}