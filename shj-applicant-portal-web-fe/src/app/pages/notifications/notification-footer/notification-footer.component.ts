import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Page} from "@shared/model";

@Component({
  selector: 'app-notification-footer',
  templateUrl: './notification-footer.component.html',
  styleUrls: ['./notification-footer.component.scss']
})
export class NotificationFooterComponent implements OnInit {

  @Input()
  page: Page;

  @Output()
  public onLoadPage: EventEmitter<any[]> = new EventEmitter<any[]>();

  constructor() { }

  ngOnInit(): void {
  }

  loadPage(page) {
    this.onLoadPage.emit(page);
  }

}
