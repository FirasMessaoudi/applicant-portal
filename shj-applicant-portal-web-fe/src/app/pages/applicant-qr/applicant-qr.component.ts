import {Component, OnInit} from '@angular/core';
import {LoginLayoutComponent} from "@core/_layout";

@Component({
  selector: 'app-applicant-qr',
  templateUrl: './applicant-qr.component.html',
  styleUrls: ['./applicant-qr.component.scss'],
  providers: [LoginLayoutComponent]
})
export class ApplicantQrComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
