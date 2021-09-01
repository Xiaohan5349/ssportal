import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./@components/home/home.component";
import {LoginComponent} from "./@components/login/login.component";
import {AuthGuard} from "./@auth/auth.guard";

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'users', loadChildren: () => import('./@components/users/users.module').then(m => m.UsersModule)},
  {path: '**', redirectTo: 'home'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
