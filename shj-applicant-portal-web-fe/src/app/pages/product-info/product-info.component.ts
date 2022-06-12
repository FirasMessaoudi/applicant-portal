import {Component, OnInit} from '@angular/core';
import {LoginLayoutComponent} from "@core/_layout";
import {TranslateService} from "@ngx-translate/core";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Component({
  selector: 'app-product-info',
  templateUrl: './product-info.component.html',
  styleUrls: ['./product-info.component.scss'],
  providers: [LoginLayoutComponent]
})
export class ProductInfoComponent implements OnInit {

  constructor(
    public translate: TranslateService,
    public sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
  }

  getTranslatedHtml(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.translate.instant('product-info.description_text'));
  }
}
