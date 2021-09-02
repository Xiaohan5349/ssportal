import { Component } from '@angular/core';
import {NbMenuItem, NbSidebarService} from "@nebular/theme";
import {HelperService} from "./@core/services/helper.service";
import {JwtAuthService} from "./@core/services/jwt-auth.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  sessionUser;
  loggedIn = false;
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

  userMenu = [{ title: 'Log out', link: '/logout' }]

  constructor(private readonly sidebarService: NbSidebarService, private helperService: HelperService, private jwtAuth: JwtAuthService) {
    this.helperService.isUserLoggedIn.subscribe(res => {
      if (res) {
        this.loggedIn = true;
        this.sessionUser = this.jwtAuth.getUser();
      } else {
        this.loggedIn = false;
      }
    })
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle();
    return false;
  }
}
