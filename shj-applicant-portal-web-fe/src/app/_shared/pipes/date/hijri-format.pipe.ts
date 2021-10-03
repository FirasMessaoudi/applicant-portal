import {Pipe, PipeTransform} from '@angular/core';
import {I18nService} from "@dcc-commons-ng/services";

@Pipe({
  name: 'hijriDateFormat'
})
export class HijriFormatPipe implements PipeTransform {


  /**
   * Formats the value (numeric like 14401015) to a date format (1440/10/15)
   *
   * @param value the field value
   * @param args the field arguments
   * @returns the formatted value
   */
  constructor(private i18nService: I18nService) {
  }

  transform(value: any, args?: any): any {
    let result = value + '' || '';

    if (isNaN(value) || result.length != 8) {
      return '';
    }
    if (this.currentLanguage.startsWith("ar"))
      return result.substring(0, 4) + '/' + result.substring(4, 6) + '/' + result.substring(6, 8);

    return result.substring(6, 8) + '/' + result.substring(4, 6) + '/' + result.substring(0, 4);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

}
