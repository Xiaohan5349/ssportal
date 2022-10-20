import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";

@Component({
  selector: 'app-otp-validater',
  templateUrl: './otp-validater.component.html',
  styleUrls: ['./otp-validater.component.scss']
})
export class OtpValidaterComponent implements OnInit {
  title;
  message;
  qrcode;
  code;
  qrText;
  sessionId;
  pairingStatusSubscription: Subscription;
  deviceTested = false;
  userName;
  otp;
  error = false;
  spAlias;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }


  testDesktop() {
    this.pingidService.finalOfflineAuth(this.sessionId,this.userName,this.otp,this.spAlias).subscribe( 
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.deviceTested = true;
        } else {
          this.deviceTested = false;
          this.error = true;
          this.message = "OTP is invalide, Please re-start Test"
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
