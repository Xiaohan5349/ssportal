import { Component, OnInit } from '@angular/core';
import {JwtAuthService} from "../../@core/services/jwt-auth.service";
import {HelperService} from "../../@core/services/helper.service";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(private jwtAuth: JwtAuthService, private helperService: HelperService) { }

  ngOnInit(): void {
    this.jwtAuth.signout();
  }
}
