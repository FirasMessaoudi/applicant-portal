import { Component, OnInit } from '@angular/core';
import {$animations} from "@shared/animate/animate.animations";
import { CustomI18nService } from '@app/_core/utilities/custom-i18n.service';

@Component({
  selector: 'app-dcc-layout-qr',
  templateUrl: './dcc-layout-qr.component.html',
  styleUrls: ['./dcc-layout-qr.component.scss'],
  animations: $animations
})
export class DccLayoutQrComponent implements OnInit {
  sideTextKey: string = 'login.side_info';
  sideBtnKey: string = 'general.btn_more';
  childComponent: any;

  constructor(private i18nService: CustomI18nService) { }

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

