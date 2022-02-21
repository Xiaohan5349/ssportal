import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";
import {environment} from "../../../../environments/environment";


@Component({
  selector: 'app-home-qr-code',
  templateUrl: './home-qr-code.component.html',
  styleUrls: ['./home-qr-code.component.scss']
})
export class HomeQrCodeComponent implements OnInit, OnDestroy {
  title;
  message;
  qrcode;
  code;
  qrText;
  pairingStatusSubscription: Subscription;
  devicePaired = false;
  user;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService, private http: HttpClient) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  ngOnInit(): void {


    this.qrText = this.qrcode;

    this.pairingStatusSubscription = timer(0, 3000).pipe(
      switchMap(() => this.pingidService.checkPairingStatus(this.code, this.user.username))
    ).subscribe(res => {
      const result:any = res;
      if (result.pairingStatus.toLowerCase() === 'paired') {
        this.pairingStatusSubscription.unsubscribe();
        this.devicePaired = true;
        this.http.get(`${environment.apiURL}/mail/self?user=${this.user.sub}&task=pairdeviceself`)
        //this.http.get(`http://localhost:8080/sso/mail/self?user=user.12&task=pairdeviceself`)
      }
    }, error => {
      console.log(error)
    });
  }


  ngOnDestroy() {
    this.pairingStatusSubscription.unsubscribe();
  }
}
