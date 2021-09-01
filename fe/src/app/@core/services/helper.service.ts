import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HelperService {

  private enableSidePanel = new BehaviorSubject(null);
  shouldEnableSidePanel = this.enableSidePanel.asObservable();

  constructor() { }

  changeSidePanelDisplay(enabled) {
    this.enableSidePanel.next(enabled);
  }
}
