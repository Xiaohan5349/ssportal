import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  NbActionsModule,
  NbAlertModule,
  NbButtonModule,
  NbCardModule,
  NbContextMenuModule,
  NbDialogModule,
  NbDialogService,
  NbIconModule,
  NbInputModule,
  NbLayoutModule,
  NbMenuModule,
  NbSidebarModule,
  NbThemeModule,
  NbUserModule
} from '@nebular/theme';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {HomeComponent} from './@components/home/home.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginService} from "./@core/services/login.service";
import {LoginComponent} from './@components/login/login.component';
import {AuthGuard} from "./@auth/auth.guard";
import {JwtAuthService} from "./@core/services/jwt-auth.service";
import {LocalStoreService} from "./@core/services/local-store.service";
import {FormsModule} from "@angular/forms";
import {HelperService} from "./@core/services/helper.service";
import {FlexLayoutModule} from "@angular/flex-layout";
import {LogoutComponent} from './@components/logout/logout.component';
import {UserService} from "./@core/services/user.service";
import {TokenInterceptor} from "./@auth/token.interceptor";
import {CommonModule} from "@angular/common";
import {HomeDialogComponent} from './@components/home/home-dialog/home-dialog.component';
import {HomeQrCodeComponent} from './@components/home/home-qr-code/home-qr-code.component';
import {NgxKjuaModule} from "ngx-kjua";
import { ServicesComponent } from './@components/services/services.component';
import { HomeQrCodeGoogleComponent } from './@components/home/home-qr-code-google/home-qr-code-google.component';
import { HomeYubikeyInputComponent } from './@components/home/home-yubikey-input/home-yubikey-input.component';
import { OtpValidaterComponent } from './@components/home/otp-validater/otp-validater.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    LogoutComponent,
    HomeDialogComponent,
    HomeQrCodeComponent,
    ServicesComponent,
    HomeQrCodeGoogleComponent,
    HomeYubikeyInputComponent,
    OtpValidaterComponent
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
    NbThemeModule.forRoot({name: 'corporate'}),
    NbInputModule,
    NbButtonModule,
    NbLayoutModule,
    NbEvaIconsModule,
    HttpClientModule,
    NgxKjuaModule
  ],
  providers: [
    // REQUIRED IF YOU USE JWT AUTHENTICATION
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    LoginService, AuthGuard, JwtAuthService, LocalStoreService,
    HelperService, UserService, NbDialogService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
