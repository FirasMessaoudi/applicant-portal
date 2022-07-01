import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {LoginLayoutComponent} from "@core/_layout";

@Component({
  selector: 'app-staff-qr',
  templateUrl: './staff-qr.component.html',
  styleUrls: ['./staff-qr.component.scss'],
  providers: [LoginLayoutComponent]
})
export class StaffQrComponent implements AfterViewInit {

  @ViewChild('appleLink', { read: ElementRef }) appleLink: ElementRef;
  @ViewChild('googleLink', { read: ElementRef }) googleLink: ElementRef;


  constructor() {
  }

  ngAfterViewInit(): void {
    let ua = navigator.userAgent.toLowerCase();
    let isAndroid = ua.indexOf("android") > -1 && ua.indexOf("mobile") > -1;
    let isiPhone = ua.indexOf("iphone") > -1 && ua.indexOf("mobile") > -1;
    let isiPad = ua.indexOf("ipad") > -1 && ua.indexOf("mobile") > -1;
    if (isAndroid) {
      this.googleLink.nativeElement.click();
    } else if (isiPhone || isiPad) {
      this.appleLink.nativeElement.click();
    } else {
      // do nothing
    }
  }


}
