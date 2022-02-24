import { Subject } from 'rxjs';
import { HomeYubikeyInputComponent } from './../home/home-yubikey-input/home-yubikey-input.component';
import { OtpValidaterComponent } from './../home/otp-validater/otp-validater.component';
import { mailService } from './../../@core/services/mail.service';
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
import { AppConst } from './../../@core/utils/app-const';
import { environment } from './../../../environments/environment';

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
  //items = [{ title: 'ByPass MFA' }, { title: 'Enable User' }, { title: 'Disable User' }];
  softToken: string = "true";
  hardToken: string = "true";
  desktopToken: string = "true";
  otpToken: string = "true";
  sessionUser;
  userTokenType;

  constructor(
    private userService: UserService,
    private jwtAuth: JwtAuthService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService,
    private http: HttpClient,
    private menuService: NbMenuService,
    private mailService: mailService
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
        this.pingidService.unpairDevice(device.deviceId, this.user.userName).subscribe(
          res => {
            this.mailService.adminServiceMail(this.user.userName,AppConst.MAIL_TASK_helpDeskUnPair,this.sessionUser.sub);
          //   this.http.get(`${environment.apiURL}/mail?user=${this.user.userName}&task=${AppConst.MAIL_TASK_helpDeskUnPair}&admin=${this.sessionUser.sub}`).subscribe(res => {
          //   });
          // console.log(this.user.userName);
          // console.log(AppConst.MAIL_TASK_helpDeskUnPair);
          // console.log(this.sessionUser.sub);
          // console.log("admin email sent");  
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
        console.log(this.activationCode);
        if(this.activationCode) {
        this.dialogService.open(HomeQrCodeComponent, {
          context: {
            title: 'Register ' + type + ' Device',
            message: 'Please scan the QR code with your PingID ' + type + ' app or input paring code manually.',
            code: this.activationCode,
            qrcode: 'https://idpxnyl3m.pingidentity.com/pingid/QRRedirection?' + btoa(this.activationCode),
            userName: this.user.userName,
            mailTask: AppConst.MAIL_TASK_helpDeskPair,
            adminUser: this.sessionUser.sub,
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
            this.searchUser();
          }
        });
      } else {
        this.dialogService.open(HomeDialogComponent, {
          context: {
            title: 'Max Number of Device',
            message: 'You have reach MAX number of device'
          },
          hasBackdrop: true,
        }).onClose.subscribe(res => {
          if (res) {
              }
            }
            )
      }
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

  yubikeyStartPairing() {
    this.dialogService.open(HomeYubikeyInputComponent, {
      context: {
        title: 'Register ' + ' Authenticator',
        message: 'Please input paring code manually.',
        userName: this.user.userName,
        adminUser: this.sessionUser.sub,
        mailTask: AppConst.MAIL_TASK_helpDeskPair,
      },
      hasBackdrop: true,
      closeOnBackdropClick: false
    }).onClose.subscribe(res => {
      if (res) {
        this.searchUser();
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
        console.log(this.pairingKey);
        if (this.pairingKey) {
        this.dialogService.open(HomeQrCodeGoogleComponent, {
          context: {
            title: 'Register ' + ' Authenticator',
            message: 'Please scan the QR code with your Authenticator ' + ' app or input paring code manually.',
            qrcode: this.pairingKeyUri,
            code: this.pairingKey,
            sessionId: this.sessionId,
            user: this.user,
            userName: this.user.userName,
            adminUser: this.sessionUser.sub,
            mailTask: AppConst.MAIL_TASK_helpDeskPair,    
          },
          hasBackdrop: true,
          closeOnBackdropClick: false
        }).onClose.subscribe(res => {
          if (res) {
          }
        });
      } else {
        this.dialogService.open(HomeDialogComponent, {
          context: {
            title: 'Max Number of Device',
            message: 'You have reach MAX number of device'
          },
          hasBackdrop: true,
        }).onClose.subscribe(res => {
          if (res) {
              }
            }
            )
      }
      }, error => {
        console.log(error);
      }
    )
  }


  testMFA(testDevice) {
    this.mfaTriggered = true;
    this.pingidService.testMFA(testDevice.deviceId, this.user.userName).subscribe(
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



  testMFADesktop(testDevice){
    //need modify starOfflineAuth method
    this.pingidService.startOfflineAuth(testDevice.deviceId, this.user.userName).subscribe(
      res => {
        const result: any = res;
        console.log(result.sessionId + "offlineAuth sessionId");
        if (result.sessionId) {
          this.dialogService.open(OtpValidaterComponent, {
            context: {
              title: 'OTP Verification',
              message: 'Please input OTP generated from your desktop device',
              sessionId: result.sessionId,
              userName: this.user.userName,
            },
            hasBackdrop: true,
          }).onClose.subscribe(res => {
            if (res) {
              this.searchUser();
              }
            }, error => {
              console.log(error);
             }
            )
        } else {
          this.mfaRejected = true;
          this.mfaErrMsg = result.errorMsg;
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

  clearAlerts() {
    this.mfaRejected = false;
    this.mfaApproved = false;
    this.mfaTriggered = false;
    this.userNotFound = false;
    this.userBypassed = false;
    this.userActivat = false;
    this.userSuspend = false;
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
    this.http.get(`${environment.apiURL}/profile?user=${this.userName}`).subscribe(res => {
      this.userTokenType = res;
      console.log('token type resposne')
      console.log(this.userTokenType);
      this.hardToken = this.userTokenType.hardToken.toString();
      this.softToken = this.userTokenType.softToken.toString();
      this.desktopToken = this.userTokenType.desktopToken.toString();
      this.otpToken = this.userTokenType.otpToken.toString();
      console.log("token type showup");
      console.log(this.hardToken);
      console.log(this.softToken);
      console.log(this.desktopToken);
      console.log(this.otpToken);
      }, error => {
      }
    )
    //this.softToken = this.user.softToken;
    //this.hardToken = this.user.hardToken;
    //this.desktopToken = this.user.desktopToken;
  }

  searchUserToken(){
    this.userService.getUserTokenTypeFromLDAP(this.userName).subscrbe(
      res => {
        this.userTokenType = res;
        this.hardToken = this.userTokenType.hardToken.toString()
        this.softToken = this.userTokenType.softToken.toString()
        this.desktopToken = this.userTokenType.desktopToken.toString()
        this.otpToken = this.userTokenType.otpToken.toString()
      }, error => {
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
          this.userActivat = true;
          this.mailService.adminServiceMail(this.user.userName,AppConst.MAIL_TASK_enable,this.sessionUser.sub);
        //   this.http.get(`${environment.apiURL}/mail?user=${this.user.userName}&task=${AppConst.MAIL_TASK_enable}&admin=${this.sessionUser.sub}`).subscribe(res => {
        //   });
        // console.log(this.user.userName);
        // console.log(AppConst.MAIL_TASK_enable);
        // console.log("admin email sent");  
          this.searchUser();
        } else {
          this.mfaErrMsg = result.errorMsg;
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
          this.userSuspend = true;
          this.mailService.adminServiceMail(this.user.userName,AppConst.MAIL_TASK_disable,this.sessionUser.sub);
        //   this.http.get(`${environment.apiURL}/mail?user=${this.user.userName}&task=${AppConst.MAIL_TASK_disable}&admin=${this.sessionUser.sub}`).subscribe(res => {
        //   });
        // console.log(this.user.userName);
        // console.log(AppConst.MAIL_TASK_disable);
        // console.log("admin email sent");  
          this.searchUser();
        } else {
          this.mfaErrMsg = result.errorMsg;
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
          console.log("userBypassed")
          this.userBypassed = true;
          this.mailService.adminServiceMail(this.user.userName,AppConst.MAIL_TASK_bypass,this.sessionUser.sub);
        //   this.http.get(`${environment.apiURL}/mail?user=${this.user.userName}&task=${AppConst.MAIL_TASK_bypass}&admin=${this.sessionUser.sub}`).subscribe(res => {
        //   });
        // console.log(this.user.userName);
        // console.log(AppConst.MAIL_TASK_bypass);
        // console.log("admin email sent");  
          this.searchUser();
        } else {
          this.mfaErrMsg = result.errorMsg;
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
    }
   )
  }


  ngOnInit(): void {
    this.AdminStatus = this.jwtAuth.getAdmin();
    // this.menuService.onItemClick().subscribe((event) => {
    //   if (event.item.title === 'ByPass MFA') {
    //     this.ToggleUserBypass();
    //     console.log('ByPass MFA clicked service');
    //   }
    //   if (event.item.title === 'Enable User') {
    //     this.ActivateUser();
    //     console.log('Enable User clicked service');
    //   }
    //   if (event.item.title === 'Disable User') {
    //     this.SuspendUser();
    //     console.log('Disable User clicked service');
    //   }
    // });

  }
}
