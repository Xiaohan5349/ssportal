import {Component, OnInit} from '@angular/core';
import {UserService} from "../../@core/services/user.service";
import {NbDialogService} from "@nebular/theme";
import {HomeDialogComponent} from "./home-dialog/home-dialog.component";
import {PingIdService} from "../../@core/services/pingid.service";
import {HomeQrCodeComponent} from "./home-qr-code/home-qr-code.component";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user;
  activationCode;
  deviceList = [];
  primaryDevice;
  mfaTriggered = false;
  mfaApproved = false;
  mfaRejected = false;

  constructor(
    private userService: UserService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService,
    private http: HttpClient
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
        this.pingidService.unpairDevice(device.deviceId).subscribe(
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
    this.userService.getUserDetails().subscribe(
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
    this.pingidService.getActivationCode(type).subscribe(
      res => {
        const result: any = res;
        this.activationCode = result.activationCode;
        this.dialogService.open(HomeQrCodeComponent, {
          context: {
            title: 'Register Device',
            message: 'Please scan the QR code with your PingID mobile app or input paring code manually.',
            code: this.activationCode
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
    this.pingidService.testMFA(this.primaryDevice.deviceId).subscribe(
      res => {
        const result: any = res;
        if (result.errorMsg) {
          this.mfaRejected = true;
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
  }

  ngOnInit(): void {
    this.getUserDetails();
  }
}
