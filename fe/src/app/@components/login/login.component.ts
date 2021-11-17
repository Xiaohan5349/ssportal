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

  }
}
