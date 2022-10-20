import { mailService } from './../../../@core/services/mail.service';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";


@Component({
  selector: 'app-home-yubikey-input',
  templateUrl: './home-yubikey-input.component.html',
  styleUrls: ['./home-yubikey-input.component.scss']
})
export class HomeYubikeyInputComponent implements OnInit {
  title;
  message;
  pairingStatusSubscription: Subscription;
  devicePaired = false;
  user;
  userName;
  otp;
  adminUser;
  mailTask;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService, private mailService: mailService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  pairYubikey() {
    this.pingidService.yubikeyPairing(this.userName, this.otp).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.devicePaired = true;
          if (this.mailTask=="pairdeviceself") {
            this.mailService.selfServiceMail(this.userName,this.mailTask);
          } else if (this.mailTask=="pairdevice"){
            this.mailService.adminServiceMail(this.userName,this.mailTask,this.adminUser);
          } else{
            console.log("Email gose wrong!!");
          }  
        }
      }, error => {
        console.log(error)
      }
    )      
  }

  ngOnInit(): void {
  }

  

}
