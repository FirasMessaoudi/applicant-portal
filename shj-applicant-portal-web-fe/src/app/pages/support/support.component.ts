import {Component, Inject, OnInit} from '@angular/core';

import {environment} from 'environments/environment';
import {I18nService} from "@dcc-commons-ng/services";
import {Location} from "@angular/common";
import {ToastService} from "@shared/components/toast/toast-service";
import {TranslateService} from "@ngx-translate/core";
import {LoginLayoutComponent} from "@core/_layout";

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.scss'],
  providers: [LoginLayoutComponent]
})
export class SupportComponent implements OnInit {

  version: string = environment.version;

  public sideTextKey: string = 'support.header_text';
  public sideBtnKey: string = 'general.btn_back';

  constructor(@Inject(LoginLayoutComponent) private parent: LoginLayoutComponent,
              private i18nService: I18nService,
              private translate: TranslateService,
              private location: Location,
              private toastr: ToastService) {
  }

  ngOnInit(): void {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  public sideAction() {
    this.location.back();
  }

  copyToClipboard(text: string) {
    navigator.clipboard.writeText(text).then(() =>
      this.toastr.success(this.translate.instant('support.text-copied'), '')
    ).catch(e => console.error(e));
  }
}
