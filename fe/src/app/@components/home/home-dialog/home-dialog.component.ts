import { Component, OnInit } from '@angular/core';
import {NbDialogRef} from "@nebular/theme";

@Component({
  selector: 'app-home-dialog',
  templateUrl: './home-dialog.component.html',
  styleUrls: ['./home-dialog.component.scss']
})
export class HomeDialogComponent implements OnInit {
  title;
  message;

  constructor(private dialogRef: NbDialogRef<any>) { }

  closeDialog(res) {
    this.dialogRef.close(res);
  }

  ngOnInit(): void {
  }

}
