import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HelperService {

  private userLoggedIn = new BehaviorSubject(null);
  isUserLoggedIn = this.userLoggedIn.asObservable();

  constructor() { }

  changeUserLoggedIn(loggedIn) {
    this.userLoggedIn.next(loggedIn);
  }
}
