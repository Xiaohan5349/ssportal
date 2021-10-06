import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import {JwtAuthService} from "../@core/services/jwt-auth.service";
import {HelperService} from "../@core/services/helper.service";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private jwtAuth: JwtAuthService, private helperService: HelperService
              ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const user = this.jwtAuth.getUser();
    if (this.jwtAuth.isLoggedIn()) {
      this.helperService.changeUserLoggedIn(true);
      return true;
    } else {
      this.helperService.changeUserLoggedIn(false);
      this.router.navigate(['/login'], {
        queryParams: {
          return: state.url
        }
      });
      return false;
    }
  }
}
// export class AdminGuard implements CanAdmin {
  
//   canAdmin(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//     const adminStatus = this.jwtAuth.getAdmin();
//     if (adminStatus == 'test') {
//       this.helperService.changeUserLoggedIn(true);
//       return true;
//     } else {
//       this.helperService.changeUserLoggedIn(false);
//       this.router.navigate(['/login'], {
//         queryParams: {
//           return: state.url
//         }
//       });
//       return false;
//     }
//   }
// }
