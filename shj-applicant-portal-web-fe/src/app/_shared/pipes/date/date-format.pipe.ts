import {Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from '@angular/common';
import {I18nService} from "@dcc-commons-ng/services";

@Pipe({
  name: 'gregorianDateFormat'
})
export class DateFormatPipe implements PipeTransform {
  static readonly DATE_FMT = 'dd/MM/yyyy';
  static readonly DATE_FMT_AR = 'yyyy/MM/dd';

  constructor(private i18nService: I18nService) {
  }

  transform(
    date: Date | string
  ): string {
    if (this.currentLanguage.startsWith("ar"))
      return new DatePipe('en-US').transform(date, DateFormatPipe.DATE_FMT_AR);

    return new DatePipe('en-US').transform(date, DateFormatPipe.DATE_FMT);


  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }
}
