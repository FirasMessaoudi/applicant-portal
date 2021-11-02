import {Component, Input, OnInit} from "@angular/core";
import {UserService} from "@core/services";
import * as momentjs from 'moment';
import {I18nService} from "@dcc-commons-ng/services";
import {NotificationService} from "@core/services/notification/notification.service";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import {UserNewNotificationsCount} from "@model/user-new-notifications-count.model";

const moment = momentjs;

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {

  @Input()
  notifications: DetailedUserNotification[];
  userNewNotificationsCount: UserNewNotificationsCount;

  constructor(private userService: UserService,
              private notificationService: NotificationService,
              private i18nService: I18nService) {
  }

  ngOnInit(): void {
    this.notificationService.currentUserNewNotificationsCount.subscribe(updatedCount => {
      this.userNewNotificationsCount = updatedCount;
    });
  }

  markAsRead(notification: DetailedUserNotification, index: number) {
    if (notification.statusCode != "READ") {
      this.notificationService.markAsRead(notification.id).subscribe(data => {
        if (data > 0) {
          this.notifications[index].statusCode = "READ";
          this.notificationService.countUserNewNotifications().subscribe(notificationsCount => {
            this.notificationService.updateUserNewNotificationsCount(notificationsCount);
          });
        }
      });
    }
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

  buildRoute(nameCode) {
    switch (nameCode) {
      case 'PASSWORD_EXPIRATION':
        return "/change-password";
      case 'DAILY_SURVEY':
        return "/";
      case 'OUT_ARAFAT_FENCE':
        return "/hajj-rituals";
      default:
        return "/";
    }
  }

  buildIcon(categoryCode, important): String {
    if (important) {
      return "state-default";
    }
    switch (categoryCode) {
      case 'GENERAL':
        return "state-general";
      case 'HEALTH':
        return "state-health";
      case 'RELIGIOUS':
        return "state-religious";
      case 'RITUAL':
        return "state-ritual";
      case 'GENERAL_AWARENESS':
        return "state-info";
      default:
        return "state-general";
    }
  }

}
