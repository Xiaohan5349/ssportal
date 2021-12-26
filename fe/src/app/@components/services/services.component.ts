import { Component, OnInit } from '@angular/core';
import {UserService} from "../../@core/services/user.service";
import {NbDialogService} from "@nebular/theme";
import {PingIdService} from "../../@core/services/pingid.service";
import {HttpClient} from "@angular/common/http";
import {HomeDialogComponent} from "../home/home-dialog/home-dialog.component";
import {HomeQrCodeComponent} from "../home/home-qr-code/home-qr-code.component";
import {HomeQrCodeGoogleComponent} from "../home/home-qr-code-google/home-qr-code-google.component";
import {JwtAuthService} from "../../@core/services/jwt-auth.service";
import {NbMenuService} from '@nebular/theme';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.scss']
})
export class ServicesComponent implements OnInit {
  user;
  activationCode;
  deviceList = [];
  primaryDevice;
  mfaTriggered = false;
  mfaApproved = false;
  mfaRejected = false;
  userFound = false;
  userName;
  userNotFound = false;
  errMsg;
  AdminStatus;
  mfaErrMsg;
  pairingKeyUri;
  pairingKey;
  sessionId;
  userActivat = false;
  userSuspend = false;
  userBypassed = false;
  items = [{ title: 'ByPass MFA' }, { title: 'Enable User' }, { title: 'Disable User' }];

  constructor(
    private userService: UserService,
    private jwtAuth: JwtAuthService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService,
    private http: HttpClient,
    private menuService: NbMenuService
  ) {
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
        this.pingidService.unpairDevice(device.deviceId, this.user.userName).subscribe(
          res => {
            this.searchUser();
          }, error => {
            console.log(error);
          }
        )
      }
    });
  }

  getActivationCode(type) {
    this.pingidService.getActivationCode(type, this.user.userName).subscribe(
      res => {
        const result: any = res;
        this.activationCode = result.activationCode;
        this.dialogService.open(HomeQrCodeComponent, {
          context: {
            title: 'Register ' + type + ' Device',
            message: 'Please scan the QR code with your PingID ' + type + ' app or input paring code manually.',
            code: this.activationCode,
            user: this.user
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
            this.searchUser();
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
        this.pingidService.makeDevicePrimary(device.deviceId, this.user.userName).subscribe(
          res => {
            this.searchUser();
          }, error => {
            console.log(error);
          }
        )
      }
    });


  }

  AuthenticatorAppStartPairing() {
    this.pingidService.AuthenticatorAppStartPairing(this.user.userName).subscribe(
      res => {
        const result: any = res;
        this.pairingKeyUri = result.pairingKeyUri;
        this.pairingKey = result.pairingKey;
        this.sessionId = result.sessionId;
        this.dialogService.open(HomeQrCodeGoogleComponent, {
          context: {
            title: 'Register ' + ' Authenticator',
            message: 'Please scan the QR code with your Authenticator ' + ' app or input paring code manually.',
            qrcode: this.pairingKeyUri,
            code: this.pairingKey,
            sessionId: this.sessionId,
            user: this.user
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


  testMFA() {
    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].deviceRole.toLowerCase() === 'primary') {
        this.primaryDevice = this.deviceList[i];
      }
    }

    this.mfaTriggered = true;
    this.pingidService.testMFA(this.primaryDevice.deviceId, this.user.userName).subscribe(
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

  clearAlerts() {
    this.mfaRejected = false;
    this.mfaApproved = false;
    this.mfaTriggered = false;
    this.userNotFound = false;
  }

  searchUser() {
    this.userService.getUserDetailsByUsername(this.userName).subscribe(
      res => {
        this.user = res;
        this.deviceList = this.user.devicesDetails;
        this.userFound = true;
      }, error => {
        this.userNotFound = true;
        this.userFound = false;
        console.log(error);
        this.errMsg = error.error;
        setTimeout(() =>
          {
            this.clearAlerts();
          },
          5000);
      }
    )
  }

  ActivateUser() {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Enable User',
        message: 'Are you sure you want to enable this user?'
      },
      hasBackdrop: true,
    }).onClose.subscribe(res => {
      if (res) {
      this.pingidService.ActivateUser(this.userName).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.ngOnInit();
          this.userActivat = true;
        } else {
          this.mfaErrMsg = result.errorMsg;
        }
      }, error => {
        console.log(error);
        }
       )
      }
     }
    )
   }

  SuspendUser() {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Disable User',
        message: 'Are you sure you want to disable this user?'
      },
      hasBackdrop: true,
    }).onClose.subscribe(res => {
      if (res) {
    this.pingidService.SuspendUser(this.userName).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.ngOnInit();
          this.userSuspend = true;
        } else {
          this.mfaErrMsg = result.errorMsg;
        }
      }, error => {
        console.log(error);
       }
      )
     }
    }
   )
  }


  ToggleUserBypass() {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Bypass User MFA',
        message: 'Are you sure you want to bypass MFA for this user?'
      },
      hasBackdrop: true,
    }).onClose.subscribe(res => {
      if (res) {
    this.pingidService.ToggleUserBypass(this.userName).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.ngOnInit();
          console.log("userBypassed")
          this.userBypassed = true;
        } else {
          this.mfaErrMsg = result.errorMsg;
        }
      }, error => {
        console.log(error);
       }
      )
     }
    }
   )
  }


  ngOnInit(): void {
    this.AdminStatus = this.jwtAuth.getAdmin();
    this.menuService.onItemClick().subscribe((event) => {
      if (event.item.title === 'ByPass MFA') {
        this.ToggleUserBypass();
        console.log('ByPass MFA clicked');
      }
      if (event.item.title === 'Enable User') {
        this.ActivateUser();
        console.log('Enable User clicked');
      }
      if (event.item.title === 'Disable User') {
        this.SuspendUser();
        console.log('Disable User clicked');
      }
    });

  }
}
