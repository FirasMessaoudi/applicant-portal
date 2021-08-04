import {Directive} from '@angular/core';
import {NgbInputDatepicker} from "@ng-bootstrap/ng-bootstrap";
import {TranslateService} from "@ngx-translate/core";

@Directive({
  selector: '[appNgbDatepickerI18nTitle]'
})
export class NgbDatepickerI18nTitleDirective {

  constructor(
    private datepicker: NgbInputDatepicker,
    private translate: TranslateService,
  ) {
    const previousToggle = this.datepicker.toggle;
    this.datepicker.toggle = () => {
      previousToggle.bind(this.datepicker)();
      if (this.datepicker.isOpen()) {
        this.swapTitles();
      }
    };
  }

  swapTitles(): void {

    //change select Month title
    // @ts-ignore
    const selectMonth = this.datepicker._cRef.location.nativeElement.querySelector('select[title="Select month"]');
    selectMonth.setAttribute('title', this.translate.instant('general.select-month'));
    selectMonth.setAttribute('aria-label', this.translate.instant('general.select-month'));

    //change select Year title
    // @ts-ignore
    const selectYear = this.datepicker._cRef.location.nativeElement.querySelector('select[title="Select year"]');
    selectYear.setAttribute('title', this.translate.instant('general.select-year'));
    selectYear.setAttribute('aria-label', this.translate.instant('general.select-year'));

    // @ts-ignore
    const previousMonth = this.datepicker._cRef.location.nativeElement.querySelector('button[title="Previous month"]');
    previousMonth.setAttribute('title', this.translate.instant('general.previous-month'));
    previousMonth.setAttribute('aria-label', this.translate.instant('general.previous-month'));

    // @ts-ignore
    const nextMonth = this.datepicker._cRef.location.nativeElement.querySelector('button[title="Next month"]');
    nextMonth.setAttribute('title', this.translate.instant('general.next-month'));
    nextMonth.setAttribute('aria-label', this.translate.instant('general.next-month'));

  }
}
