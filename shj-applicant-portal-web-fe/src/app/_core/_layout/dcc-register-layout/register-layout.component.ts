import {Component, OnInit} from '@angular/core';
import {$animations} from "@shared/animate/animate.animations";

@Component({
  selector: 'app-register-layout',
  templateUrl: './register-layout.component.html',
  styleUrls: ['./register-layout.component.scss'],
  animations: $animations
})
export class RegisterLayoutComponent implements OnInit {

  sideTextKey: string = 'login.side_info';
  sideBtnKey: string = 'general.btn_more';
  childComponent: any;

  constructor() { }

  ngOnInit() {
  }

  onActivate(event: any) {
    this.childComponent = event;
    this.sideTextKey = (event && event.sideTextKey) ? event.sideTextKey : 'login.side_info';
    this.sideBtnKey = (event && event.sideBtnKey) ? event.sideBtnKey : 'general.btn_more';

  }

  sideAction() {
    if (this.childComponent && this.childComponent.sideAction) {
      this.childComponent.sideAction();
    } else {
      // common action to be done here
    }
  }
}
