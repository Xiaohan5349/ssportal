import { Injectable } from '@angular/core';
import {AppConst} from "../utils/app-const";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PingIdService {
  BACKEND_URL: string = AppConst.SERVER_PATH;

  constructor(
    private http: HttpClient
  ) { }

  unpairDevice(deviceId) {
    const url = this.BACKEND_URL + '/pingid/unpairDevice';
    const payload = {
      deviceId: deviceId
    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }
}
