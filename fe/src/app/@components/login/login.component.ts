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
  serverPath = AppConst.serverPath;
  errorMsg;
  credential = {'username': '', 'password': ''};

  constructor(
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

  ngOnInit(): void {
  }
}
