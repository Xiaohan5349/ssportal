import { mailService } from './../../../@core/services/mail.service';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Subscription, timer} from "rxjs";
import {switchMap} from "rxjs/operators";
import {PingIdService} from "../../../@core/services/pingid.service";
import { HomeDialogComponent } from '../home-dialog/home-dialog.component';
import {NbDialogService} from "@nebular/theme";


@Component({
  selector: 'app-home-sms-input',
  templateUrl: './home-sms-input.component.html',
  styleUrls: ['./home-sms-input.component.scss']
})
export class HomeSmsInputComponent implements OnInit {
  title;
  message;
  pairingStatusSubscription: Subscription;
  devicePaired = false;
  user;
  userName;
  smsNumber = "";
  smsNumberNotValid;
  adminUser;
  mailTask;
  smsSent = false;
  otpNeed = false;
  otp;
  sessionId;

  constructor(private dialogRef: NbDialogRef<any>, private pingidService: PingIdService, private dialogService: NbDialogService, private mailService: mailService) {
  }

  closeDialog(res) {
    this.dialogRef.close(res);
  }


  validPhone(smsNumber){
    var phoneno = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;
    if(smsNumber.match(phoneno))
          {
            this.smsNumberNotValid = false;
          }
        else
          {
            this.smsNumberNotValid = true;
          }  
  }

  pairSMS(){
    this.validPhone(this.smsNumber);
    if(!this.smsNumberNotValid){
      this.pingidService.startOfflinePairing(this.smsNumber,this.userName).subscribe(
        res => {
          const result: any = res;
          this.sessionId = result.sessionId;
          if(this.sessionId){
            this.smsSent = true;
            this.otpNeed = true;
            this.message = "Please Input One Time Password send to " + this.smsNumber;
        } else {
          this.dialogService.open(HomeDialogComponent, {
            context: {
              title: 'Max Number of Device',
              message: 'You have reach MAX number of device'
            },
            hasBackdrop: true,
          }).onClose.subscribe(res => {
              }
              )
        }
        }, error => {
          console.log(error);
        }
      )

    }
  }

  otpValidate(){
    this.pingidService.finalizeOfflinePairing(this.sessionId, this.otp).subscribe(
      res => {
        const result: any = res;
        if (result.errorId == "200") {
          this.otpNeed = false;
          this.devicePaired = true;
          if (this.mailTask=="pairdeviceself") {
            this.mailService.selfServiceMail(this.userName,this.mailTask);
          } else if (this.mailTask=="pairdevice"){
            this.mailService.adminServiceMail(this.userName,this.mailTask,this.adminUser);
          } else{
          }  
        } else if (result.errorId == "20513") {
          this.message = "Incorrect OTP. Please try again.";
        }
      }, error => {
        console.log(error)
      }
    )      
  }



  ngOnInit(): void {
  }

  

}
