import { Component, OnInit } from '@angular/core';
import {$animations} from "@shared/animate/animate.animations";
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-dcc-layout-landing',
  templateUrl: './dcc-layout-landing.component.html',
  styleUrls: ['./dcc-layout-landing.component.scss'],
  animations: $animations
})
export class DccLayoutLandingComponent implements OnInit {
  sideTextKey: string = 'login.side_info';
  sideBtnKey: string = 'general.btn_more';
  childComponent: any;

  constructor(private i18nService: I18nService) { }

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
  
  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }
}
