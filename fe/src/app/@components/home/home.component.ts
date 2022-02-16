import { HomeYubikeyInputComponent } from './home-yubikey-input/home-yubikey-input.component';
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
import {HomeQrCodeGoogleComponent} from "./home-qr-code-google/home-qr-code-google.component";
import {NbMenuService} from '@nebular/theme';

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
  pairingKeyUri;
  pairingKey;
  sessionId;
  AdminStatus;
  softToken: string = "false";
  hardToken: string = "false";
  desktopToken: string = "false";
  otpToken: string = "false";
  orgUuid = 'dffd9656-dfb8-4a0b-bb35-8590e62984e4';
  idpAccountId
  items = [{ title: 'ByPass MFA' }, { title: 'Enable User' }, { title: 'Disable User' }];

//need modify this page to display correct user info. user info from jwt should consist with user info in PingID

  constructor(
    private userService: UserService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService,
    private http: HttpClient,
    private jwtAuth: JwtAuthService,
    private menuService: NbMenuService
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
        this.pingidService.unpairDevice(device.deviceId, this.sessionUser.sub).subscribe(
          res => {
          }, error => {
            console.log(error);
          }
        )
      }
    });
  }

  getUserDetails() {
    this.userService.getUserDetailsByUsername(this.sessionUser.sub).subscribe(
      res => {
        this.user = res;
        this.deviceList = this.user.devicesDetails;
        console.log(this.user);
        console.log(this.deviceList);
      }, error => {
        console.log(error);
      }
    );
  }

  getActivationCode(type) {
    this.pingidService.getActivationCode(type, this.sessionUser.sub).subscribe(
      res => {
        const result: any = res;
        this.activationCode = result.activationCode;
        console.log(this.activationCode);
        this.dialogService.open(HomeQrCodeComponent, {
          context: {
            title: 'Register ' + type + ' Device',
            message: 'Please scan the QR code with your PingID ' + type + ' app or input paring code manually.',
            code: this.activationCode,
            qrcode: 'https://idpxnyl3m.pingidentity.com/pingid/QRRedirection?' + btoa(this.activationCode),
            user: this.sessionUser
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
          }
        });
      }, error => {
        console.log(error);
      }
    )
  }

   AuthenticatorAppStartPairing() {
      this.pingidService.AuthenticatorAppStartPairing(this.sessionUser.sub).subscribe(
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
              user: this.sessionUser
            },
            hasBackdrop: true,
            closeOnBackdropClick: false
          }).onClose.subscribe(res => {
            if (res) {
            }
          });
        }, error => {
          console.log(error);
        }
      )
    // this.dialogService.open(HomeQrCodeGoogleComponent, {
    //   context: {
    //     title: 'Register ' + ' Authenticator',
    //     message: 'Please scan the QR code with your Authenticator ' + ' app or input paring code manually.',
    //     qrcode: this.pairingKeyUri,
    //     code: this.pairingKey,
    //     sessionId: this.sessionId,
    //     user: this.sessionUser
    //   },
    //   hasBackdrop: true,
    //   closeOnBackdropClick: false
    // }).onClose.subscribe(res => {
    //   if (res) {
    //     this.ngOnInit();
    //   }
    // })
  }

  yubikeyStartPairing() {
        this.dialogService.open(HomeYubikeyInputComponent, {
          context: {
            title: 'Register ' + ' Authenticator',
            message: 'Please input paring code manually.',
            user: this.sessionUser
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
          }
        });
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
        this.pingidService.makeDevicePrimary(device.deviceId, this.sessionUser.sub).subscribe(
          res => {
          }, error => {
            console.log(error);
          }
        )
      }
    });


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
      this.pingidService.ActivateUser(this.sessionUser.sub).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
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
    this.pingidService.SuspendUser(this.sessionUser.sub).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
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
    this.pingidService.ToggleUserBypass(this.sessionUser.sub).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          console.log("userBypassed")
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


  testMFA(testDevice) {
    this.mfaTriggered = true;
    this.pingidService.testMFA(testDevice.deviceId, this.sessionUser.sub).subscribe(
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

/*  testMFA() {
    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].deviceRole.toLowerCase() === 'primary') {
        this.primaryDevice = this.deviceList[i];
      }
    }

    this.mfaTriggered = true;
    this.pingidService.testMFA(this.primaryDevice.deviceId, this.sessionUser.sub).subscribe(
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
*/

  testWebMFA() {
    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].deviceRole.toLowerCase() === 'primary') {
        this.primaryDevice = this.deviceList[i];
      }
    }

    this.pingidService.testWebMFA(this.primaryDevice.deviceId, this.sessionUser.sub).subscribe(
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

  test(){
    console.log("test1")
  }

  ngOnInit(): void {
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
    this.getUserDetails();
    this.AdminStatus = this.jwtAuth.getAdmin();
    this.softToken = this.sessionUser.softToken;
    this.hardToken = this.sessionUser.hardToken;
    this.desktopToken = this.sessionUser.desktopToken;
    this.otpToken = this.sessionUser.otpToken;
    console.log(this.AdminStatus);
    console.log(this.sessionUser.sub);

  }

}
