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
      if (this.jwtAuth.isLoggedIn()) {
        const user = this.jwtAuth.getUser();
        this.jwtAuth.setAdmin();
        if (this.jwtAuth.isAdmin()) {
          this.helperService.changeUserAdminStatus(true);
          this.helperService.changeUserLoggedIn(true);
          console.log("admin user")
          return true;
        }else{
          this.helperService.changeUserAdminStatus(false);
          this.helperService.changeUserLoggedIn(true);
          this.router.navigate(['/home'], {
          });  
          console.log("not admin user")
          return false;
        }
      } else {
        this.helperService.changeUserLoggedIn(false);
        this.router.navigate(['/login'], {
        });
        console.log("not login")
        return false;
      }
  }
}
