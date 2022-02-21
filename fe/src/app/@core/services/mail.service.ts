import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import {map, catchError, delay} from 'rxjs/operators';
import {of, BehaviorSubject, throwError} from 'rxjs';
import {User} from "../models/user";
import {AppConst} from "../utils/app-const";
import {LocalStoreService} from "./local-store.service";
import {environment} from "../../../environments/environment";
import {HelperService} from "./helper.service";

@Injectable({
    providedIn: 'root',
})
export class mailService {
    token;
    isAuthenticated: Boolean;
    user: User;
    user$;
    signingIn: Boolean;
    return: string;
    JWT_TOKEN = AppConst.JWT_STORAGE_NAME;
    APP_USER = AppConst.APP_USER_STORAGE_NAME;
    APP_ADMIN = AppConst.APP_ADMIN_STORAGE_NAME;


    constructor(
        private ls: LocalStoreService,
        private http: HttpClient,
        private router: Router,
        private route: ActivatedRoute,
        private helperService: HelperService
    ) {
    }

    adminServiceMail (userName,MAIL_TASK,adminUser){
        this.http.get(`${environment.apiURL}/mail?user=${userName}&task=${MAIL_TASK}&admin=${adminUser}`).subscribe(res => {
        });
      console.log(userName);
      console.log(MAIL_TASK);
      console.log("admin email sent");      
    }

    selfServiceMail (userName,MAIL_TASK){
        this.http.get(`${environment.apiURL}/mail/self?user=${userName}&task=${MAIL_TASK}`).subscribe(res => {
        });
      console.log(userName);
      console.log(MAIL_TASK);
      console.log("email sent");      
    }


}
