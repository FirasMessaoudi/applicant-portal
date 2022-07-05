import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';

import {AccordionModule} from './modules/accordion/accordion.module';

// @ng-bootstrap
import {NgBootstrapModule} from '@shared/ng-bootstrap.module';
/* import {NgbModule} from '@ng-bootstrap/ng-bootstrap'; */
// SVG Icons
import {SvgIconModule} from '@app/_shared/components/svg-icon/svg-icon.module';
import {ArchwizardModule} from 'angular-archwizard';
import {ConfirmDialogComponent} from "@shared/components/confirm-dialog";
import {DateAgoPipe} from '@shared/pipes/date/date-ago/date-ago.pipe';
import {DateFormatPipe} from '@shared/pipes/date/date-format.pipe';
import {HijriFormatPipe} from '@shared/pipes/date/hijri-format.pipe';
import {DateTimeFormatPipe} from '@shared/pipes/date/date-time-format.pipe';


import {ToastsContainer} from './components/toast';
import {NgbdDatepickerIslamicumalqura} from './directives/datepicker-islamicumalqura';
import {IbanStatusPipe} from "@shared/pipes/iban/iban-status.pipe";
import {TranslateModule} from "@ngx-translate/core";
import {NgxCaptchaModule} from "ngx-captcha";
import {DccCommonsNgPipesModule} from '@dcc-commons-ng/pipes';
import {HijriGregorianDatepickerModule} from "@shared/modules/hijri-gregorian-datepicker/hijri-gregorian-datepicker.module";
import {NgMultiSelectDropDownModule} from "ng-multiselect-dropdown";
import {GoogleMapsModule} from '@angular/google-maps'
import {MonthDayHijriFormatPipe} from "@shared/pipes/date/month-day-hijri-format.pipe";
import {ArabicNumberPipe} from "@shared/pipes/numbers/arabic-number.pipe";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";

// Notification scrollbar
import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {NotificationListComponent} from "@shared/components/notification-list/notification-list.component";
import {RouterModule} from "@angular/router";
import {ModalDialogComponent} from "@shared/components/modal-dialog/modal-dialog.component";

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

@NgModule({
  declarations: [
    ToastsContainer,
    ConfirmDialogComponent,
    DateAgoPipe,
    NgbdDatepickerIslamicumalqura,
    DateFormatPipe,
    HijriFormatPipe,
    MonthDayHijriFormatPipe,
    IbanStatusPipe,
    ArabicNumberPipe,
    NotificationListComponent,
    DateTimeFormatPipe,
    ModalDialogComponent,

  ],
  imports: [
    CommonModule,
    NgBootstrapModule,
    ArchwizardModule,
    SvgIconModule,
    AccordionModule,
    TranslateModule,
    NgxCaptchaModule,
    HijriGregorianDatepickerModule,
    NgMultiSelectDropDownModule,
    GoogleMapsModule,
    NgxIntlTelInputModule,
    PerfectScrollbarModule,
    RouterModule
  ],
  providers: [
    DateAgoPipe, DatePipe, DateFormatPipe, HijriFormatPipe, MonthDayHijriFormatPipe, NgxCaptchaModule, DateTimeFormatPipe,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ],
  exports: [
    NgbdDatepickerIslamicumalqura,
    AccordionModule,
    SvgIconModule,
    NgBootstrapModule,
    ArchwizardModule,
    TranslateModule,
    NgxCaptchaModule,
    DateAgoPipe,
    ConfirmDialogComponent,
    ToastsContainer,
    DateFormatPipe,
    HijriFormatPipe,
    MonthDayHijriFormatPipe,
    DatePipe,
    IbanStatusPipe,
    ArabicNumberPipe,
    DccCommonsNgPipesModule,
    NgMultiSelectDropDownModule,
    HijriGregorianDatepickerModule,
    GoogleMapsModule,
    NgxIntlTelInputModule,
    PerfectScrollbarModule,
    NotificationListComponent,
    DateTimeFormatPipe,
    RouterModule,
    ModalDialogComponent
  ],
  entryComponents: [
    NgbdDatepickerIslamicumalqura,
    ConfirmDialogComponent
  ],

})
export class SharedModule {
}
