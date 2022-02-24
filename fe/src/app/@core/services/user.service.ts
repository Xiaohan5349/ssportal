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

  userTokenType;

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
  
  getUserTokenTypeFromLDAP(username) {
    this.http.get(`${this.BACKEND_URL}/profile?user=${username}`).subscribe(res => {
      this.userTokenType = res;
    });
      return this.userTokenType;
  }
}
