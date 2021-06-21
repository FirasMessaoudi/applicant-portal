import { Pipe, PipeTransform } from '@angular/core';
import * as moment from "moment";
import 'moment/locale/ar';

@Pipe({
  name: 'dateAgo'
})
export class DateAgoPipe implements PipeTransform {

  transform(date: Date,daysDiff?:number,format?:string, language?:string): any {


    let today = moment();
    let dateValue = moment(date);
    dateValue.locale('en');
    if(language && language.startsWith("ar")){
      dateValue.locale('ar');
    }


    let dateInFormat = format? dateValue.format(format): dateValue;
    if(daysDiff){
      let diffInDays = today.diff(dateValue, 'days');
      let duration = moment.duration(today.diff(dateValue));
      let diffInHours = duration.asHours();
      return diffInDays<=daysDiff
      ?(diffInHours<=1 ? dateValue.fromNow() : dateValue.calendar())
      : dateInFormat;

    }

    return dateInFormat;
  }


}
