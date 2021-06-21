import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ibanStatus'
})
export class IbanStatusPipe implements PipeTransform {


  /**
   * Formats the Iban status value
   *
   * @param value the field value
   * @param args the field arguments
   * @returns the formatted value
   */
  transform(value: any, args?: any): any {
    if (value) {
      if (value == 28000) {
        return "unified-profile.iban-tab.iban-status-value.new";
      } else if (value == 28001) {
        return "unified-profile.iban-tab.iban-status-value.pending";
      } else if (value == 28002) {
        return "unified-profile.iban-tab.iban-status-value.valid";
      } else if (value == 28003) {
        return "unified-profile.iban-tab.iban-status-value.invalid";
      } else if (value == 28004) {
        return "unified-profile.iban-tab.iban-status-value.dq";
      } else if (value == 28005) {
        return "unified-profile.iban-tab.iban-status-value.no_match";
      }
    } else {
      return "unified-profile.iban-tab.iban-status-value.not-available";
    }
  }
}
