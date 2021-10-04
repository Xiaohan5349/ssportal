import { Injectable } from '@angular/core';
import {AppConst} from "../utils/app-const";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../models/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  BACKEND_URL: string = AppConst.SERVER_PATH;

  constructor(
    private http: HttpClient
  ) { }

  // getUserDetails() {
  //   const url = this.BACKEND_URL + '/pingid/getUserDetails';
  //   const httpOptions = {
  //     headers: new HttpHeaders({'Content-Type': 'application/json'})
  //   };
  //   return this.http.get(url, httpOptions);
  // }

  getUserDetailsByUsername(username) {
    const url = this.BACKEND_URL + '/pingid/getUserDetailsByUsername';
    const payload = {
      username: username
    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  // addUser(user: User): Observable<any> {
  //   const url = this.BACKEND_URL + '/users';
  //   const httpOptions = {
  //     headers: new HttpHeaders({'Content-Type': 'application/json'})
  //   };
  //   return this.http.post(url, JSON.stringify(user), httpOptions);
  // }
}
