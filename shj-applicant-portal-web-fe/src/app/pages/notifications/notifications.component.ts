import {Component, OnInit} from "@angular/core";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  activeId = 1;
  tabsHeader = [
    "notification-management.private_notifications",
    "notification-management.public_notifications",
    "notification-management.view_all"
  ]

  ngOnInit() {
  }

}
