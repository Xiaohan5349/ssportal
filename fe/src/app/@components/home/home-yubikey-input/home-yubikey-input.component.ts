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
  otp;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  pairYubikey() {
    console.log(this.otp);
    console.log(this.user.sub);
    this.pingidService.yubikeyPairing(this.user.sub, this.otp).subscribe(
      res => {
        const result: any = res;
        console.log(result);
        if (result.errorId == "200") {
          this.devicePaired = true;
        }
      }, error => {
        console.log(error)
      }
    )      
  }

  ngOnInit(): void {
  }

  

}
