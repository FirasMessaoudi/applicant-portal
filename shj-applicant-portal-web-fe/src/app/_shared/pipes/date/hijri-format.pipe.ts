import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'hijriFormat'
})
export class HijriFormatPipe implements PipeTransform {


  /**
   * Formats the value (numeric like 14401015) to a date format (1440/10/15)
   *
   * @param value the field value
   * @param args the field arguments
   * @returns the formatted value
   */
  transform(value: any, args?: any): any {
    let result = value+'' || '';

    if (isNaN(value) || result.length != 8){
      return '';
    }

    return result.substring(0,4) + '/' + result.substring(4,6) + '/' + result.substring(6,8);
  }
}
