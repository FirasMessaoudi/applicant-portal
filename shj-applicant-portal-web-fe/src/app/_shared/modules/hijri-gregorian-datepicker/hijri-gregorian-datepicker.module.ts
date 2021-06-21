import { NgModule } from '@angular/core';
import { HijriGregorianDatepickerComponent } from './datepicker/hijri-gregorian-datepicker.component';
import { HijriDatepickerComponent } from './datepicker/hijri-datepicker/hijri-datepicker.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgbModule, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { DateFormatterService } from './datepicker/date-formatter.service';
import { CustomNgbDateParserFormatter } from './datepicker/CustomNgbDateParserFormatter';
import { ProvideParentFormDirective } from './datepicker/provide-parent-form.directive';

// https://eslamelmadny.github.io/HijriGregorianDatepicker/
// https://github.com/EslamElmadny/HijriGregorianDatepicker

@NgModule({
  declarations: [
    HijriGregorianDatepickerComponent,
    HijriDatepickerComponent,
    ProvideParentFormDirective
  ],
  imports: [
    CommonModule ,
    ReactiveFormsModule,
    FormsModule,
    NgbModule
  ],
  providers: [
    {provide: NgbDateParserFormatter, useClass: CustomNgbDateParserFormatter},
    DateFormatterService
  ],
  exports: [ HijriGregorianDatepickerComponent]
})
export class HijriGregorianDatepickerModule { }
