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

testRef(){
  // console.log('calling be acs');
  // this.http.get(`http://localhost:8181/ssportal/be/saml-acs?REF=909090`).subscribe(
  //   res => {
  //     console.log(res);
  //   }
  // )

  window.location.href='http://localhost:8181/ssportal/be/saml-acs?REF=909090'
}


  ngOnInit(): void {
    this.REF = this.router.url.substring(this.router.url.indexOf('=') + 1);
    console.log(this.REF);
    const httpOptions = {
      headers: new HttpHeaders({'ping.uname': 'admin', 'ping.pwd': 'Password1!', 'ping.instanceId': 'ssoSPadapter'})
    };
    this.http.get(`http://localhost:9031/ext/ref/pickup?REF=${this.REF}`, httpOptions).subscribe(
      res => { 
           console.log(res);
     }
    )
    this.ls.setItem('SSPORTAL_APP_USER', this.user.subject)
    if (this.user.subject == 'test2') {
      this.router.navigate(['/home']);
    }
  }
}
