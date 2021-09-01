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
  NbButtonModule, NbInputModule, NbAlertModule
} from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { HomeComponent } from './@components/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import {NbAuthModule, NbPasswordAuthStrategy} from "@nebular/auth";
import {LoginService} from "./@core/services/login.service";
import { LoginComponent } from './@components/login/login.component';
import {AuthGuard} from "./@auth/auth.guard";
import {JwtAuthService} from "./@core/services/jwt-auth.service";
import {LocalStoreService} from "./@core/services/local-store.service";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    NbIconModule,
    NbAlertModule,
    NbSidebarModule.forRoot(),
    NbMenuModule.forRoot(),
    NbThemeModule.forRoot(),
    NbInputModule,
    NbButtonModule,
    NbLayoutModule,
    NbEvaIconsModule,
    HttpClientModule

  ],
  providers: [LoginService, AuthGuard, JwtAuthService, LocalStoreService],
  bootstrap: [AppComponent]
})
export class AppModule { }
