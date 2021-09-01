import {Injectable} from '@angular/core';
import {AppConst} from "../helpers/app-const";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private serverPath: string = AppConst.serverPath;

  constructor(
    private http: HttpClient
    , private router: Router) {
  }

  sendCredential(username: string, password: string) {
    const url = this.serverPath + '/login';
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'})
    };

    return this.http.post(url, body.toString(), httpOptions);
  }
}
