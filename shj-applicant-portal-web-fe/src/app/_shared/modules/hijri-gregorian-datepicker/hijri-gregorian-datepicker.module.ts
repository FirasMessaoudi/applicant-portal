import {NgModule} from '@angular/core';
import {HijriGregorianDatepickerComponent} from './datepicker/hijri-gregorian-datepicker.component';
import {HijriDatepickerComponent} from './datepicker/hijri-datepicker/hijri-datepicker.component';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {DateFormatterService} from './datepicker/date-formatter.service';
import {CustomNgbDateParserFormatter} from './datepicker/CustomNgbDateParserFormatter';
import {ProvideParentFormDirective} from './datepicker/provide-parent-form.directive';
import {NgbDatepickerI18nTitleDirective} from "@shared/modules/hijri-gregorian-datepicker/ngb-datepicker-i18n-title.directive";
import {TranslateModule} from "@ngx-translate/core";

// https://eslamelmadny.github.io/HijriGregorianDatepicker/
// https://github.com/EslamElmadny/HijriGregorianDatepicker

@NgModule({
  declarations: [
    HijriGregorianDatepickerComponent,
    HijriDatepickerComponent,
    ProvideParentFormDirective,
    NgbDatepickerI18nTitleDirective
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgbModule,
    TranslateModule
  ],
  providers: [
    {provide: NgbDateParserFormatter, useClass: CustomNgbDateParserFormatter},
    DateFormatterService
  ],
  exports: [ HijriGregorianDatepickerComponent]
})
export class HijriGregorianDatepickerModule { }
