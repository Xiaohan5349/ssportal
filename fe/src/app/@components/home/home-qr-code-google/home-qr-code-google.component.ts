import { mailService } from './../../../@core/services/mail.service';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";

@Component({
  selector: 'app-home-qr-code-google',
  templateUrl: './home-qr-code-google.component.html',
  styleUrls: ['./home-qr-code-google.component.scss']
})
export class HomeQrCodeGoogleComponent implements OnInit {
  title;
  message;
  qrcode;
  code;
  qrText;
  sessionId;
  pairingStatusSubscription: Subscription;
  devicePaired = false;
  user;
  otp;
  mailTask;
  userName;
  adminUser;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService, private mailService: mailService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  pairDevice() {
    this.pingidService.AuthenticatorAppFinishPairing(this.sessionId, this.otp).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.devicePaired = true;
          if (this.mailTask=="pairdeviceself") {
            this.mailService.selfServiceMail(this.userName,this.mailTask);
          } else if (this.mailTask=="pairdevice"){
            this.mailService.adminServiceMail(this.userName,this.mailTask,this.adminUser);
          } else{
          }  
        }
      }, error => {
        console.log(error)
      }
    )      
  }

  ngOnInit(): void {
    this.qrText = this.qrcode;
  }

  

}
