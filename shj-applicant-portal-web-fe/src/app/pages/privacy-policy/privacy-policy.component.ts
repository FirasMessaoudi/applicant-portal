import {Component, OnInit} from '@angular/core';
import {LoginLayoutComponent} from "@core/_layout";
import {TranslateService} from "@ngx-translate/core";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Component({
  selector: 'app-privacy-policy',
  templateUrl: './privacy-policy.component.html',
  styleUrls: ['./privacy-policy.component.scss'],
  host: {
    class:'privacy-policy'
  },
  providers: [LoginLayoutComponent]
})
export class PrivacyPolicyComponent implements OnInit {

  constructor(
    public translate: TranslateService,
    public sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
  }

  getTranslatedHtml(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.translate.instant('privacy-policy.privacy_text'));
  }

}
