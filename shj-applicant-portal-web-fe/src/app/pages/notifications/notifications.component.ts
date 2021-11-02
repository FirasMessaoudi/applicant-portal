import {Component, OnInit} from "@angular/core";
import {UserService} from "@core/services";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import * as momentjs from 'moment';
import {I18nService} from "@dcc-commons-ng/services";
import {NotificationService} from "@core/services/notification/notification.service";
import {UserNewNotificationsCount} from "@model/user-new-notifications-count.model";

const moment = momentjs;

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  activeId = 3;
  notifications: DetailedUserNotification[] = [];
  page: any;
  userNewNotificationsCount: UserNewNotificationsCount;

  constructor(private userService: UserService,
              private notificationService: NotificationService,
              private i18nService: I18nService) {
  }

  ngOnInit() {
    this.notificationService.currentUserNewNotificationsCount.subscribe(updatedCount => {
      this.userNewNotificationsCount = updatedCount;
    });
    this.loadNotifications();
  }

  loadNotifications() {
    this.notificationService.getNotifications().subscribe(data => {
      this.notifications = data;
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

  loadPage(number: number) {

  }

  getPrivateNotifications() {
    return this.notifications.filter(notification => notification.userSpecific);
  }

  getPublicNotifications() {
    return this.notifications.filter(notification => !notification.userSpecific);
  }
}
