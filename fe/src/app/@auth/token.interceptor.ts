import {Injectable} from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest, HttpResponse, HttpErrorResponse
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Router} from '@angular/router';
import 'rxjs/add/operator/do';
import {AppConst} from "../@core/utils/app-const";
import {JwtAuthService} from "../@core/services/jwt-auth.service";
import {LocalStoreService} from "../@core/services/local-store.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    JWT_TOKEN = AppConst.JWT_STORAGE_NAME;
    APP_USER = AppConst.APP_USER_STORAGE_NAME;

    constructor(private jwtAuth: JwtAuthService, private router: Router, private ls: LocalStoreService) {
    }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const token = this.jwtAuth.getJwtToken();
        let changedReq;

        if (token) {
            changedReq = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                },
            });
        } else {
            changedReq = req;
        }

        return next.handle(changedReq).do((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                // do stuff with response if you want
            }
        }, (err: any) => {
            if (err instanceof HttpErrorResponse) {
                if (err.status === 401 ) {
                    console.log(err.message);
                    this.ls.setItem(this.JWT_TOKEN, null);
                    this.ls.setItem(this.APP_USER, null);
                    // redirect to the login route
                    this.router.navigate(['/login']);
                }
            }
        });
    }
}
