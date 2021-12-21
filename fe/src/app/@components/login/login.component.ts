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
  RE_LOGIN_URL: string = AppConst.LOGIN_PATH;
  serverPath = AppConst.SERVER_PATH;
  errorMsg;
  REF;
  RES;
  Admin : boolean;
  noAdmin : boolean;
  adminJwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYzgxODgzIiwic2NvcGVzIjpbXSwiaXNzIjoiU2VsZiBTZXJ2aWNlIFBvcnRhbCIsIm1haWwiOiJ4aWFvY2hhbi5saUBjb25zdWx0YW50LmJlZGJhdGguY29tIiwiYWRtaW4iOiJhZG1pbiIsImlhdCI6MTY0MDAzNTYzMCwiZXhwIjoxNjQwMDUzNjMwfQ.xi0bO4ZviGGCravHs0-9gaLkMuL_uUCfRpOUbQUcMbA";
  helpdeskJwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyLjEyIiwic2NvcGVzIjpbXSwiaXNzIjoiU2VsZiBTZXJ2aWNlIFBvcnRhbCIsIm1haWwiOiJ1c2VyLjEyQGV4YW1wbGUuY29tIiwiYWRtaW4iOiJoZWxwZGVzayIsImlhdCI6MTYzOTc1NjE5NiwiZXhwIjoxNjM5Nzc0MTk2fQ.q4JBOrMISo0dMekNZWTPwMS_OOYNC704Ppsq472-z_E";
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


onRedirect(){
  window.location.href=this.RE_LOGIN_URL;
}


ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.REF = p;
      this.REF = this.REF.REF;
      this.http.get(`${environment.apiURL}/authenticate?REF=${this.REF}`).subscribe(res => {
        this.RES=res;
        this.jwtAuth.setToken(this.RES.token);
        this.router.navigate(['/home']);
        }, err => {
          this.errorMsg = 'Please Login';
        })
    });
//   this.jwtAuth.setToken(this.adminJwt);
//   this.router.navigate(['/home']);
  }
}
