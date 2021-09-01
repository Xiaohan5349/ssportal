import {Component, OnInit} from '@angular/core';
import {AppConst} from "../../@core/helpers/app-const";
import {Router} from "@angular/router";
import {LoginService} from "../../@core/services/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  busy: boolean = false;

  serverPath = AppConst.serverPath;
  loginError: boolean = false;
  loggedIn = false;
  credential = {'username': '', 'password': ''};
  emailSent: boolean = false;
  usernameExists: boolean = false;
  emailExists: boolean = false;
  username: string = '';
  email: string = '';

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {
  }

  onLogin() {
    this.busy = true;

    if (this.credential.username.trim() == "" || this.credential.password.trim() == "") {
      this.busy = false;
      this.loggedIn = false;
      this.loginError = true;
    } else {
      this.loginService.sendCredential(this.credential.username, this.credential.password).subscribe(
        res => {
          const result: any = res;
          let user = result.json();
          console.log(user);
          this.loggedIn = true;
          let role = user.role[0].authority;

          if (role == 'ROLE_USER') {
            this.router.navigate(['/dashboard']);
          }

          if (role == 'ROLE_ADMIN') {
            this.router.navigate(['/adminDashboard']);
          }
        },
        error => {
          this.busy = false;
          this.loggedIn = false;
          this.loginError = true;
        }
      );
    }
  }

  ngOnInit(): void {
  }
}
