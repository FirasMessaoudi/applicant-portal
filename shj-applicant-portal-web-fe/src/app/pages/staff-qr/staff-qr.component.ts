import {Component, OnInit} from '@angular/core';
import {LoginLayoutComponent} from "@core/_layout";

@Component({
  selector: 'app-staff-qr',
  templateUrl: './staff-qr.component.html',
  styleUrls: ['./staff-qr.component.scss'],
  providers: [LoginLayoutComponent]
})
export class StaffQrComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
