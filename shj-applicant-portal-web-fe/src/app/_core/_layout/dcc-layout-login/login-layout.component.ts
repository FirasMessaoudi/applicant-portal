import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import { Title } from '@angular/platform-browser';
import { CustomI18nService } from '@app/_core/utilities/custom-i18n.service';
import { TranslateService } from '@ngx-translate/core';
import {$animations} from '@shared/animate/animate.animations';

@Component({
  selector: 'dcc-login-layout',
  templateUrl: './login-layout.component.html',
  styleUrls: ['./login-layout.component.scss'],
  animations: $animations
})

export class LoginLayoutComponent implements OnInit {

  sideTextKey: string = 'login.side_info';
  sideBtnKey: string = 'general.btn_more';
  childComponent: any;

  constructor(private i18nService: CustomI18nService, private titleService: Title, private translate: TranslateService) {  
    this.setLanguage();
    this.titleService.setTitle(this.translate.instant('general.app_title'));
  }

  setLanguage() {
    this.i18nService.language = this.currentLanguage;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

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
