import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "@shared/shared.module";
import {NotificationsRoutingModule} from "@pages/notifications/notifications-routing.module";
import {NotificationsComponent} from "@pages/notifications/notifications.component";
import {NotificationListComponent} from "@pages/notifications/notification-list/notification-list.component";

@NgModule({
  imports: [
    CommonModule,
    NotificationsRoutingModule,
    TranslateModule,
    SharedModule
  ],
  exports: [
    NotificationListComponent
  ],
  declarations: [NotificationsComponent, NotificationListComponent]
})
export class NotificationsModule {
}
