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
import {ActivatedRoute} from '@angular/router'


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  serverPath = AppConst.SERVER_PATH;
  errorMsg;
  credential = {'username': '', 'password': ''};
  REF;
  test = '%2Fhome%3FRefID%3D3C6EE6BB683E147E72DAA9C026600B435ED7BE0F22';
  log;
  user = {
    "partnerEntityID": "pfTest",
    "instanceId": "mfaRef",
    "mail": "test@example.com",
    "subject": "test",
    "authnCtx": "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified",
    "sessionid": "123456",
    "authnInst": "2021-09-29 14:37:47-0400",
    "admin": "false"
  };
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
  jwtAdmin = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXJ0bmVyRW50aXR5SUQiOiJwZlRlc3QiLCJpbnN0YW5jZUlkIjoibWZhUmVmIiwibWFpbCI6InRlc3RBZG1pbkBleGFtcGxlLmNvbSIsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJhdXRobkN0eCI6InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOnVuc3BlY2lmaWVkIiwic2Vzc2lvbmlkIjoiNjU0MzIxIiwiYXV0aG5JbnN0IjoiMjAyMS0wOS0yOSAxNDozNzo0Ny0wNDAwIiwiYWRtaW4iOiJ0cnVlIn0.8a6vobZRLfOPBwv1KbCjLKkAuZ9Qx3WOKul9lc1d634';
  jwtUser = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXJ0bmVyRW50aXR5SUQiOiJwZlRlc3QiLCJpbnN0YW5jZUlkIjoibWZhUmVmIiwibWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwiYXV0aG5DdHgiOiJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YWM6Y2xhc3Nlczp1bnNwZWNpZmllZCIsInNlc3Npb25pZCI6IjEyMzQ1NiIsImF1dGhuSW5zdCI6IjIwMjEtMDktMjkgMTQ6Mzc6NDctMDQwMCIsImFkbWluIjoiZmFsc2UifQ.eca4biwABarQNuj7laPBUasMw8kZEA-Qx5fHe96iBMs';
  jwtOut;

  constructor(
    private ls:LocalStoreService,
    private loginService: LoginService,
    private router: Router,
    private jwtAuth: JwtAuthService,
    private http: HttpClient,
    private helperService: HelperService,
    private route: ActivatedRoute
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


// showNewsDetailData() {
//   this.route.queryParams.subscribe(p => {
//     const newsUrl = p.newsUrl // 获取参数
//     console.log(newsUrl);
//   });
// }

  ngOnInit(): void {

    this.route.queryParams.subscribe(p => {
      this.REF = p;
      this.REF = this.REF.REF;
      console.log(this.REF);
      this.http.get(`${environment.apiURL}/authenticate?REF=${this.REF}`).subscribe(res => {
        this.jwtAuth.setToken(res['result'].token);
        this.router.navigate(['/home']);
        // const httpOptions = {
        //   headers: new HttpHeaders({'Authorization': 'Bearer ' + res['result'].token})
        // };
          //     this.http.get(`http://openam2.example.com:8080/sso/user/getCurrentUser`, httpOptions).subscribe(
          //   user => {
          //     const me = <User> user;
          //     // me.roles = [];
          //     // for (let i = 0; i < authorities.length; i++) {
          //     //   const role = authorities[i].authority;
          //     //   me.roles.push(role);
          //     // }
          //     console.log(me);
          //     this.jwtAuth.setUserAndToken(res['result'].token, me, !!res);

          //     this.router.navigateByUrl(this.jwtAuth.return);
          //   }, error => {
          //   }
          // );
        }, err => {
          this.errorMsg = 'Invalid JWT. Please try again.';
        })
    });

    // this.REF = window.location.href.substring(this.test.length - 42);
    // console.log(this.REF);
    // this.http.get(`http://openam2.example.com:8080/sso/authenticate?REF=${this.REF}`).subscribe(res => {
    //   this.jwtAuth.setToken(res['result'].token);
    //   const httpOptions = {
    //     headers: new HttpHeaders({'Authorization': 'Bearer ' + res['result'].token})
    //   };
    //         this.http.get(`http://openam2.example.com:8080/sso/user/getCurrentUser`, httpOptions).subscribe(
    //       user => {
    //         const me = <User> user;
    //         // me.roles = [];
    //         // for (let i = 0; i < authorities.length; i++) {
    //         //   const role = authorities[i].authority;
    //         //   me.roles.push(role);
    //         // }
    //         console.log(me);
    //         this.jwtAuth.setUserAndToken(res['result'].token, me, !!res);

    //         this.router.navigateByUrl(this.jwtAuth.return);
    //       }, error => {
    //       }
    //     );
    //   }, err => {
    //     this.errorMsg = 'Invalid JWT. Please try again.';
    //   })
    // // *ngif='Admin' html

    // //  this.ls.setItem('SSPORTAL_APP_USER', this.user)
    // // if (this.user.subject == 'testAdm') {
    // //   this.Admin=true;
    // // //this.router.navigate(['/services']);
    // // }else{
    // //   this.noAdmin=true;
    // // //this.router.navigate(['/home']);
    // // }
  }
}
