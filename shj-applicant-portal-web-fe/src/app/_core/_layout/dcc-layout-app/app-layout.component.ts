import { Component, ElementRef } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { CustomI18nService } from '@app/_core/utilities/custom-i18n.service';
import { TranslateService } from '@ngx-translate/core';
import { $animations } from '@shared/animate/animate.animations';

@Component({
  selector: 'dcc-app-layout',
  templateUrl: './app-layout.component.html',
  styleUrls: ['./app-layout.component.scss'],
  host: { 'class': 'dcc__wrapper' },
  animations: $animations
})
export class AppLayoutComponent {
  state = 'normal';
  private navClasses = {
    normal: 'wrapper-collapse',
    collapsed: 'wrapper-expand'
  };
  constructor(private el: ElementRef, private i18nService: CustomI18nService, private titleService: Title, private translate: TranslateService) { 
    this.setLanguage();
    this.titleService.setTitle(this.translate.instant('general.app_title'));

  }

  setLanguage() {
    this.i18nService.language = this.currentLanguage;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }


  toggleNavbar() {
    let bodyClass = '';

    if (this.state === 'normal') {
      this.state = 'collapse';
      bodyClass = this.navClasses.collapsed;
    } else {
      this.state = 'normal';
      bodyClass = this.navClasses.normal;
    }

    this.el.nativeElement.closest('body').className = bodyClass;
  }

}
