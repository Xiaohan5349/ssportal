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
export class JwtAuthService {
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
        this.route.queryParams
            .subscribe(params => this.return = params['return'] || '/home');
        this.user$ = (new BehaviorSubject<User>(this.user));
    }

    public signin(username, password) {
        // return of({token: DEMO_TOKEN, user: DEMO_USER})
        //     .pipe(
        //         delay(1000),
        //         map((res: any) => {
        //             this.setUserAndToken(res.token, res.user, !!res);
        //             this.signingIn = false;
        //             return res;
        //         }),
        //         catchError((error) => {
        //             return throwError(error);
        //         })
        //     );

        // FOLLOWING CODE SENDS SIGNIN REQUEST TO SERVER

        this.signingIn = true;
        return this.http.post(`${environment.apiURL}/authenticate`, {username, password})
            .pipe(
                map((res: any) => {
                    this.signingIn = false;
                    return res;
                }), catchError((error) => {
                    return throwError(error);
                })
            );
    }

    /*
      checkTokenIsValid is called inside constructor of
      shared/components/layouts/admin-layout/admin-layout.component.ts
    */
    public checkTokenIsValid() {
        // return of(DEMO_USER)
        //     .pipe(
        //         map((profile: User) => {
        //             this.setUserAndToken(this.getJwtToken(), profile, true);
        //             this.signingIn = false;
        //             return profile;
        //         }),
        //         catchError((error) => {
        //             return of(error);
        //         })
        //     );

        /*
          The following code get user data and jwt token is assigned to
          Request header using token.interceptor
          This checks if the existing token is valid when app is reloaded
        */

        return this.http.get(`${environment.apiURL}/token/get-current-user`)
            .pipe(
                map((profile: User) => {
                    const me = <User>profile;
                    const authorities: any[] = me['authorities'];

                    // me.roles = [];
                    // for (let i = 0; i < authorities.length; i++) {
                    //     me.roles.push(authorities[i].authority);
                    // }
                    this.setUserAndToken(this.getJwtToken(), profile, true);
                    return profile;
                }),
                catchError((error) => {
                    this.signout();
                    return of(error);
                })
            );
    }

    public signout() {
        this.cleanUserAndToken();
        this.router.navigateByUrl('/home');
    }

    isLoggedIn(): Boolean {
        return !!this.getJwtToken();
    }
    isAdmin(): Boolean {
        if (this.getAdmin() == "helpdesk" || this.getAdmin() == "admin"){
            return true
        }else{
            return false
        }    
    }

    getJwtToken() {
        const token = this.ls.getItem(this.JWT_TOKEN);
        return token;
    }

    getUser() {
        const user = this.ls.getItem(this.APP_USER);
        return user;
    }

    getAdmin(){
        const admin = this.ls.getItem(this.APP_ADMIN);
        return admin;
    }

    setAdmin(){
        const jwt = this.ls.getItem(this.JWT_TOKEN);
        console.log(this.JWT_TOKEN);
        console.log("setAdmin" + jwt);
        //add check JWT method with BE
        this.parseJwt(jwt);
        const user = this.ls.getItem(this.APP_USER);
        this.ls.setItem(this.APP_ADMIN,user.admin);
    }


    setToken(token: String) {
        this.ls.setItem(this.JWT_TOKEN, token);
        console.log("settoken method" + token);
    }

    setUserAndToken(token: String, user: User, isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated;
        this.token = token;
        this.user = user;
        this.user$.next(user);
        this.ls.setItem(this.JWT_TOKEN, token);
        this.ls.setItem(this.APP_USER, user);
    }

    parseJwt (jwt) {
        console.log(jwt);
        const base64Url = jwt.split('.')[1];
        console.log(base64Url);
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        console.log(base64);
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        console.log(jsonPayload);
        const jwtOut = JSON.parse(jsonPayload);
        this.ls.setItem(this.APP_USER, jwtOut);
        console.log(jwtOut);
    }
    cleanUserAndToken() {
        this.ls.setItem(this.JWT_TOKEN, null);
        this.ls.setItem(this.APP_USER, null);
        this.ls.setItem(this.APP_ADMIN, null);
    }
}
