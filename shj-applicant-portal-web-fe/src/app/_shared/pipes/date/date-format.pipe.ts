import {Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from '@angular/common';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe extends DatePipe implements PipeTransform {

  static readonly DATE_FMT = 'dd/MM/yyyy';

  constructor(){
    super('en-US');}

  transform(value: any, args?: any): any {
    return super.transform(value, DateFormatPipe.DATE_FMT);
  }
}
