import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'arabicNumber'})
export class ArabicNumberPipe implements PipeTransform {

  /**
   * Formats the hijri season value
   *
   * @param number the hijri season year
   * @returns the formatted value
   */
  transform(number: number): string {
    if (number === null || number === undefined) {
      return '';
    }
    return new Intl.NumberFormat('ar-SA', {})
      .format(number)
      .replace("٬", "");
  }
}
