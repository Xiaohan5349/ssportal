import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import {JwtAuthService} from "../@core/services/jwt-auth.service";
import {HelperService} from "../@core/services/helper.service";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {  
  constructor(private router: Router, private jwtAuth: JwtAuthService, private helperService: HelperService
    ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    //need modify
    const user = this.jwtAuth.getUser();
    this.jwtAuth.setAdmin();
      if (this.jwtAuth.isLoggedIn() && this.jwtAuth.isAdmin()) {
        this.helperService.changeUserLoggedIn(true);
        this.helperService.changeUserAdminStatus(true);
        console.log("admin user");
        return true;
      } else if (this.jwtAuth.isLoggedIn() && !this.jwtAuth.isAdmin()) {
          this.helperService.changeUserLoggedIn(true);
          this.helperService.changeUserAdminStatus(false);
          this.router.navigate(['/home'], {
          });
          console.log("not admin user");
          return false;
        } else {
            this.helperService.changeUserLoggedIn(false);
            this.helperService.changeUserAdminStatus(false);
            this.router.navigate(['/home'], {
            });
            console.log("not log in");
            return false;
        }
  }
}
