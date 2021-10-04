import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";

@Component({
  selector: 'app-home-qr-code',
  templateUrl: './home-qr-code.component.html',
  styleUrls: ['./home-qr-code.component.scss']
})
export class HomeQrCodeComponent implements OnInit, OnDestroy {
  title;
  message;
  code;
  qrText;
  pairingStatusSubscription: Subscription;
  devicePaired = false;
  user;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  ngOnInit(): void {
    this.qrText = 'https://idpxnyl3m.pingidentity.com/pingid/QRRedirection?' + btoa(this.code);

    this.pairingStatusSubscription = timer(0, 3000).pipe(
      switchMap(() => this.pingidService.checkPairingStatus(this.code, this.user.username))
    ).subscribe(res => {
      const result:any = res;
      if (result.pairingStatus.toLowerCase() === 'paired') {
        this.pairingStatusSubscription.unsubscribe();
        this.devicePaired = true;
      }
    }, error => {
      console.log(error)
    });
  }

  ngOnDestroy() {
    this.pairingStatusSubscription.unsubscribe();
  }
}
