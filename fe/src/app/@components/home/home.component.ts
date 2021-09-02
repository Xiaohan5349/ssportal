import {Component, OnInit} from '@angular/core';
import {UserService} from "../../@core/services/user.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user;

  constructor(
    private userService: UserService
  ) {
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
