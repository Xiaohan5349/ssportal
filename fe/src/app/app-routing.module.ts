import {AdminGuard} from './@auth/admin.guard';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./@components/home/home.component";
import {LoginComponent} from "./@components/login/login.component";
import {AuthGuard} from "./@auth/auth.guard";
import {LogoutComponent} from "./@components/logout/logout.component";
import {ServicesComponent} from "./@components/services/services.component";

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'services',
    component: ServicesComponent,
    canActivate: [AdminGuard]
  },
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '**', redirectTo: 'home'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
