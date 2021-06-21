import { Injectable } from '@angular/core';
import { NgbDateStruct, NgbDateParserFormatter, NgbDate } from '@ng-bootstrap/ng-bootstrap';

import * as momentjs from 'moment';
const moment = momentjs;


import * as moment_ from 'moment-hijri';
const momentHijri = moment_;

@Injectable()
export class DateFormatterService {

  constructor(private parserFormatter: NgbDateParserFormatter) { }

    fromDate(date: Date): NgbDateStruct {
      let newDate = new Date(date);
      return new NgbDate(newDate.getUTCFullYear(), newDate.getUTCMonth() + 1, newDate.getUTCDate() + 1);
    }

    toDate(dateStruct: NgbDateStruct): Date {
      return new Date(dateStruct.year, dateStruct.month - 1, dateStruct.day, 0, 0, 0, 0);
    }

    toString(date: NgbDateStruct): string {
        return this.parserFormatter.format(date);
    }

    toHijriDateStruct(hijriDate: string, format: string): NgbDate  {

      const hijriMomentDate =  momentHijri(hijriDate , format); // Parse a Hijri date based on format.

      const day = hijriMomentDate.iDate();
      const month = +hijriMomentDate.iMonth() + 1 ;
      const year = hijriMomentDate.iYear();

      const ngDate =  new NgbDate(+year, month, +day);
      return ngDate;
   }


    toGregorianDateStruct(gregorianDate: string, format: string): NgbDate {

      const momentDate = moment(gregorianDate, format); // Parse a Gregorian date based on format.

      const day = momentDate.date();
      const month = +momentDate.month() + 1;
      const year = momentDate.year();

      return new NgbDate(+year, +month, +day);
    }

    toHijri(date: NgbDateStruct): NgbDateStruct {
        if (!date) {
            return null;
        }
        const dateStr = this.toString(date);

        const momentDate = momentHijri(dateStr, 'DD/MM/YYYY');

        const day = momentDate.iDate();
        const month = +momentDate.iMonth() + 1 ;
        const year = momentDate.iYear();

        return new NgbDate(+year, month, +day);
    }

    toGregorian(date: NgbDateStruct) {
        if (!date) {
            return null;
        }

        const dateStr = this.toString(date);

        const momentDate = momentHijri(dateStr, 'iDD/iMM/iYYYY');

        const day = momentDate.locale('en').format('D');
        const month = momentDate.locale('en').format('M') ;
        const year = momentDate.locale('en').format('Y');

        return new NgbDate(+year, +month, +day);
    }

    todayHijri(): NgbDateStruct {

      const todayHijri = momentHijri().locale('en').format('iYYYY/iMM/iDD');

      return this.toHijriDateStruct(todayHijri, 'iYYYY/iMM/iDD') ;

    }

    todayGregorian(): NgbDateStruct {

      const todayGregorian = moment().locale('en').format('YYYY/MM/DD');

      return this.toGregorianDateStruct(todayGregorian, 'YYYY/MM/DD') ;
    }

}
