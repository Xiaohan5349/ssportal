import { Component } from '@angular/core';
import {NbMenuItem, NbSidebarService} from "@nebular/theme";
import {HelperService} from "./@core/services/helper.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  enableSidePanel = false;
  items: NbMenuItem[] = [
    {
      title: 'Home',
      icon: 'home-outline',
      link: '/home',
      home: true
    },
    {
      title: 'Users',
      icon: 'people-outline',
      link: '/users'
    }
  ];

  constructor(private readonly sidebarService: NbSidebarService, private helperService: HelperService) {
    this.helperService.shouldEnableSidePanel.subscribe(res => {
      if (res) {
        console.log('enabled')
        this.enableSidePanel = true;
      } else {
        console.log('disabled')
        this.enableSidePanel = false;
      }
    })
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle();
    return false;
  }
}
