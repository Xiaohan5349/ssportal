import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../@core/services/user.service";
import {NbDialogService} from "@nebular/theme";
import {HomeDialogComponent} from "./home-dialog/home-dialog.component";
import {PingIdService} from "../../@core/services/pingid.service";
import {HomeQrCodeComponent} from "./home-qr-code/home-qr-code.component";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from 'rxjs/operators';
import {JwtAuthService} from "../../@core/services/jwt-auth.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  @ViewChild('ppmForm') ppmForm: ElementRef;

  user;
  activationCode;
  deviceList = [];
  primaryDevice;
  mfaTriggered = false;
  mfaApproved = false;
  mfaRejected = false;
  sessionUser;
  mfaErrMsg;
  postUrl;
  ppmRequest;
  orgUuid = 'dffd9656-dfb8-4a0b-bb35-8590e62984e4';
  idpAccountId

//need modify this page to display correct user info. user info from jwt should consist with user info in PingID

  constructor(
    private userService: UserService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService,
    private http: HttpClient,
    private jwtAuth: JwtAuthService
  ) {
    this.sessionUser = jwtAuth.getUser();
  }

  unpairDevice(device) {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Unpair Device',
        message: 'Are you sure you want to unpair this device?'
      },
      hasBackdrop: true,
    }).onClose.subscribe(res => {
      if (res) {
        console.log(res);
        this.pingidService.unpairDevice(device.deviceId, this.sessionUser.username).subscribe(
          res => {
            this.ngOnInit();
          }, error => {
            console.log(error);
          }
        )
      }
    });
  }

  getUserDetails() {
    this.userService.getUserDetailsByUsername(this.sessionUser.username).subscribe(
      res => {
        this.user = res;
        this.deviceList = this.user.devicesDetails;
        console.log(this.user);
      }, error => {
        console.log(error);
      }
    );
  }

  getActivationCode(type) {
    this.pingidService.getActivationCode(type, this.sessionUser.username).subscribe(
      res => {
        const result: any = res;
        this.activationCode = result.activationCode;
        this.dialogService.open(HomeQrCodeComponent, {
          context: {
            title: 'Register ' + type + ' Device',
            message: 'Please scan the QR code with your PingID ' + type + ' app or input paring code manually.',
            code: this.activationCode,
            user: this.sessionUser
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
            this.ngOnInit();
          }
        });
      }, error => {
        console.log(error);
      }
    )
  }

  makeDevicePrimary (device) {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Change Primary MFA Device',
        message: 'Are you sure you want to make this one your primary MFA device?'
      },
      hasBackdrop: true,
    }).onClose.subscribe(res => {
      if (res) {
        this.pingidService.makeDevicePrimary(device.deviceId, this.sessionUser.username).subscribe(
          res => {
            this.ngOnInit();
          }, error => {
            console.log(error);
          }
        )
      }
    });


  }

  testMFA() {
    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].deviceRole.toLowerCase() === 'primary') {
        this.primaryDevice = this.deviceList[i];
      }
    }

    this.mfaTriggered = true;
    this.pingidService.testMFA(this.primaryDevice.deviceId, this.sessionUser.username).subscribe(
      res => {
        const result: any = res;
        if (result.errorMsg) {
          this.mfaRejected = true;
          this.mfaErrMsg = result.errorMsg;
          this.mfaTriggered = false;
        } else {
          this.mfaApproved = true;
          this.mfaTriggered = false;
        }

        setTimeout(() =>
          {
            this.clearAlerts();
          },
          5000);
      }, error => {
        console.log(error);
      }
    )
  }

  testWebMFA() {
    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].deviceRole.toLowerCase() === 'primary') {
        this.primaryDevice = this.deviceList[i];
      }
    }

    this.pingidService.testWebMFA(this.primaryDevice.deviceId, this.sessionUser.username).subscribe(
      res => {
        const result: any = res;
        this.postUrl = result.postUrl;
        this.ppmRequest = result.ppm;
        this.idpAccountId = result.idpAccountId;
        this.ppmForm.nativeElement.submit();

        console.log(result);
      }, error => {
        console.log(error);
      }
    )
  }

  clearAlerts() {
    this.mfaRejected = false;
    this.mfaApproved = false;
    this.mfaTriggered = false;
  }

  ngOnInit(): void {
    this.getUserDetails();
    console.log(this.sessionUser.username);
  }
}
