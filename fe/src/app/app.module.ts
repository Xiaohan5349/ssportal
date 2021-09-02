import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  NbThemeModule,
  NbLayoutModule,
  NbIconModule,
  NbSidebarModule,
  NbMenuModule,
  NbButtonModule,
  NbInputModule,
  NbAlertModule,
  NbActionsModule,
  NbUserModule,
  NbContextMenuModule,
  NbCardModule,
  NbDialogModule, NbDialogService
} from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { HomeComponent } from './@components/home/home.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NbAuthModule, NbPasswordAuthStrategy, NbUser} from "@nebular/auth";
import {LoginService} from "./@core/services/login.service";
import { LoginComponent } from './@components/login/login.component';
import {AuthGuard} from "./@auth/auth.guard";
import {JwtAuthService} from "./@core/services/jwt-auth.service";
import {LocalStoreService} from "./@core/services/local-store.service";
import {FormsModule} from "@angular/forms";
import {HelperService} from "./@core/services/helper.service";
import {FlexLayoutModule} from "@angular/flex-layout";
import { LogoutComponent } from './@components/logout/logout.component';
import {UserService} from "./@core/services/user.service";
import {TokenInterceptor} from "./@auth/token.interceptor";
import {CommonModule} from "@angular/common";
import { HomeDialogComponent } from './@components/home/home-dialog/home-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    LogoutComponent,
    HomeDialogComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    FlexLayoutModule,
    NbActionsModule,
    NbContextMenuModule,
    NbCardModule,
    NbIconModule,
    NbAlertModule,
    NbUserModule,
    NbDialogModule.forRoot(),
    NbSidebarModule.forRoot(),
    NbMenuModule.forRoot(),
    NbThemeModule.forRoot(),
    NbInputModule,
    NbButtonModule,
    NbLayoutModule,
    NbEvaIconsModule,
    HttpClientModule

  ],
  providers: [
    // REQUIRED IF YOU USE JWT AUTHENTICATION
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },,
    LoginService, AuthGuard, JwtAuthService, LocalStoreService,
    HelperService, UserService, NbDialogService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
