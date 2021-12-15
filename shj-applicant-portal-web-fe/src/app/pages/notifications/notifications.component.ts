import {Component, OnInit} from "@angular/core";
import {UserService} from "@core/services";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import * as momentjs from 'moment';
import {I18nService} from "@dcc-commons-ng/services";
import {NotificationService} from "@core/services/notification/notification.service";
import {UserNewNotificationsCount} from "@model/user-new-notifications-count.model";
import {Page} from "@shared/model";
import {Subscription} from "rxjs";

const moment = momentjs;

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  activeId = 3;
  notifications: DetailedUserNotification[] = [];
  ALL: string = "ALL";
  userNewNotificationsCount: UserNewNotificationsCount;
  pageArray: Array<number>;
  page: Page;
  listSubscription: Subscription;

  constructor(private userService: UserService,
              private notificationService: NotificationService,
              private i18nService: I18nService) {
  }

  ngOnInit() {
    this.notificationService.currentUserNewNotificationsCount.subscribe(updatedCount => {
      this.userNewNotificationsCount = updatedCount;
    });
    this.notificationService.currentUserPaginatedNotifications.subscribe(notifications => {
      this.notifications = notifications;
    });
    this.notificationService.currentUserSpecificNotifications.subscribe(notifications => {
      this.notifications = notifications;
    });
    this.notificationService.currentUserNotSpecificNotifications.subscribe(notifications => {
      this.notifications = notifications;
    });
    this.loadPage(this.ALL, 0);
  }

  ngOnDestroy() {
    this.listSubscription.unsubscribe();
  }

  loadPage(type: string, page) {
    // load data requests for param page
    this.listSubscription = this.notificationService.getTypedNotifications(type, page).subscribe(data => {
      this.page = data;
      if (this.page != null) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.notifications = this.page.content;
        if (type == this.ALL && page === 0) {
          this.notificationService.updateUserPaginatedNotifications(this.page.content);
        }
        if (type == "USER_SPECIFIC" && page === 0) {
          this.notificationService.updateUserSpecificNotifications(this.page.content);
        }
        if (type == "GENERAL" && page === 0) {
          this.notificationService.updateUserNotSpecificNotifications(this.page.content);
        }
      }
    });
  }

  pageCounter(i: number): Array<number> {
    return new Array(i);
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
