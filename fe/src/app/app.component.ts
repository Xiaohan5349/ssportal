import { Component } from '@angular/core';
import {NbMenuItem, NbSidebarService, NbThemeService} from "@nebular/theme";
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
  Admin = false;
  items: NbMenuItem[] = [
    {
      title: 'Home',
      icon: 'home-outline',
      link: '/home',
      home: true
    },
    {
      title: 'Services',
      icon: 'people-outline',
      link: '/services'
    }
  ];

  userMenu = [{ title: 'Log out', link: '/logout' }]

  constructor(
    private readonly sidebarService: NbSidebarService, 
    private helperService: HelperService, 
    private jwtAuth: JwtAuthService, 
    private themeService: NbThemeService) 
    {
    this.helperService.isUserLoggedIn.subscribe(res => {
      if (res) {
        this.loggedIn = true;
        this.sessionUser = this.jwtAuth.getUser();
        this.themeService.changeTheme('corporate');
      } else {
        this.themeService.changeTheme('corporate2');
        this.loggedIn = false;
      }
    })
    this.helperService.isuserAdminStatus.subscribe(res => {
      if (res) {
        this.Admin = true;
        this.sessionUser = this.jwtAuth.getUser();
      } else {
        this.Admin = false;
      }
    })
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle();
    return false;
  }

}
