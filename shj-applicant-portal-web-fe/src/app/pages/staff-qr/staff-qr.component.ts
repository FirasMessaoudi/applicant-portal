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
    let ua = navigator.userAgent.toLowerCase();
    let isAndroid = ua.indexOf("android") > -1 && ua.indexOf("mobile") > -1;
    let isiPhone = ua.indexOf("iphone") > -1 && ua.indexOf("mobile") > -1;
    let isiPad = ua.indexOf("ipad") > -1 && ua.indexOf("mobile") > -1;
    if (isAndroid) {
      // TODO put staff url
      //window.open('https://play.google.com/store/apps/details?id=com.elm.shaaer.staff.mobile', '_blank');
    } else if (isiPhone || isiPad) {
      // TODO put staff url
      //window.open('https://apps.apple.com/ng/app/pilgrimage-app-pilgrims/idXXXXXXXXXX', '_blank');
    } else {
      // TODO remove this once urls are available
      // document.getElementsByTagName('html') [0].remove();
    }
  }


}
