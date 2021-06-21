import {Component, EventEmitter, Injectable, Input, Output} from '@angular/core';
import {NgbCalendar, NgbCalendarIslamicUmalqura, NgbDatepickerI18n, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

const WEEKDAYS = ['ثن', 'ثل', 'أر', 'خم', 'جم', 'سب', 'أح'];
const MONTHS = ['محرم', 'صفر', 'ربيع 1', 'ربيع 2', 'جمادى 1', 'جمادى 2', 'رجب', 'شعبان', 'رمضان', 'شوال',
  'ذو القعدة', 'ذو الحجة'];

@Injectable()
export class IslamicI18n extends NgbDatepickerI18n {

  getWeekdayShortName(weekday: number) {
    return WEEKDAYS[weekday - 1];
  }

  getMonthShortName(month: number) {
    return MONTHS[month - 1];
  }

  getMonthFullName(month: number) {
    return MONTHS[month - 1];
  }

  getDayAriaLabel(date: NgbDateStruct): string {
    return `${date.day}-${date.month}-${date.year}`;
  }
}

@Component({
  selector: 'ngbd-datepicker-islamicumalqura',
  template: ` <ngb-datepicker  ngbDatepicker class="rtl" #dp [(ngModel)]="model" [firstDayOfWeek]="7"
                             [minDate]=minDate
                             [maxDate]=maxDate
                             (click)="selectDay()">
  </ngb-datepicker>
  `,
  providers: [
    {provide: NgbCalendar, useClass: NgbCalendarIslamicUmalqura},
    {provide: NgbDatepickerI18n, useClass: IslamicI18n}
  ]
})
export class NgbdDatepickerIslamicumalqura {

  model: NgbDateStruct;
  @Input() minDate:string;
  @Input() maxDate:string;
  @Output() dateSelected = new EventEmitter();

  constructor() {}

  selectDay() {
    this.dateSelected.emit(this.model);
  }

}
