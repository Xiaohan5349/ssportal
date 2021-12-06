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
export class HomeQrCodeGoogleComponent implements OnInit, OnDestroy {
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

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  pairDevice() {
    console.log(this.otp);
    console.log(this.sessionId);
    this.pingidService.AuthenticatorAppFinishPairing(this.sessionId, this.otp).subscribe(
      res => {
        const result: any = res;
        console.log(result);
        if (result.errorId == "200") {
          this.pairingStatusSubscription.unsubscribe();
          this.devicePaired = true;
        }
      }, error => {
        console.log(error)
      }
    )      
  }

  ngOnInit(): void {
    this.qrText = this.qrcode;
  }

  

  ngOnDestroy() {
    this.pairingStatusSubscription.unsubscribe();
  }
}
