import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./@components/home/home.component";
import {NbAuthComponent, NbLoginComponent, NbLogoutComponent} from "@nebular/auth";

const routes: Routes = [
  {
    path: 'auth',
    component: NbAuthComponent,
    children: [
      {
        path: '',
        component: NbLoginComponent,
      },
      {
        path: 'login',
        component: NbLoginComponent,
      },
      {
        path: 'logout',
        component: NbLogoutComponent,
      },
    ],
  },
  {
    path: 'home',
    component: HomeComponent
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
