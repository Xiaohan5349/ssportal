import {Component, OnInit} from '@angular/core';
import {UserService} from "../../@core/services/user.service";
import {NbDialogService} from "@nebular/theme";
import {HomeDialogComponent} from "./home-dialog/home-dialog.component";
import {PingIdService} from "../../@core/services/pingid.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user;

  constructor(
    private userService: UserService,
    private dialogService: NbDialogService,
    private pingidService: PingIdService
  ) {
  }

  unpairDevice(device) {
    this.dialogService.open(HomeDialogComponent, {
      context: {
        title: 'Unpair Device',
        message: 'Are you sure you want to unpair this device?'
      },
      hasBackdrop: true
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
        console.log(this.user);
      }, error => {
        console.log(error);
      }
    );
  }

  ngOnInit(): void {
    this.getUserDetails();
  }
}
