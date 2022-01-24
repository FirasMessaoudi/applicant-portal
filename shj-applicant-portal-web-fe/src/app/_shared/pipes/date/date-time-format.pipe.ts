import {Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from '@angular/common';
import {I18nService} from "@dcc-commons-ng/services";

@Pipe({
  name: 'gregorianDateTimeFormat'
})
export class DateTimeFormatPipe implements PipeTransform {
  static readonly DATE_FMT = 'dd/MM/yyyy hh:mm a';
  static readonly DATE_FMT_AR = 'yyyy/MM/dd hh:mm a';

  constructor(private i18nService: I18nService) {
  }

  transform(
    date: Date | string
  ): string {
    if (this.currentLanguage.startsWith("ar"))
      return new DatePipe('en-US').transform(date, DateTimeFormatPipe.DATE_FMT_AR);

    return new DatePipe('en-US').transform(date, DateTimeFormatPipe.DATE_FMT);


  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }
}
