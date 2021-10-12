import {Component, OnInit} from "@angular/core";
import {UserService} from "@core/services";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import * as momentjs from 'moment';
import {I18nService} from "@dcc-commons-ng/services";

const moment = momentjs;

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  activeId = 1;
  allNotifications: DetailedUserNotification[] = [];
  privateNotifications: DetailedUserNotification[] = [];
  publicNotifications: DetailedUserNotification[] = [];
  tabsHeader = [
    "notification-management.private_notifications",
    "notification-management.public_notifications",
    "notification-management.view_all"
  ]

  constructor(private userService: UserService,
              private i18nService: I18nService) {
  }

  ngOnInit() {
    this.loadNotifications();
  }

  loadNotifications() {
    this.userService.getNotifications().subscribe(data => {
      this.allNotifications = data;
      this.privateNotifications = data.filter(notification => notification.userSpecific);
      this.publicNotifications = data.filter(notification => !notification.userSpecific);
    });
  }

  getRelativeTime(date: Date) {
    let dateValue = moment(date);
    dateValue.locale('en');
    if (this.currentLanguage.startsWith('ar')) {
      dateValue.locale('ar-TN').fromNow();
    }
    return dateValue.fromNow();
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }
}
