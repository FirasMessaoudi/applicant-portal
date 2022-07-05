import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/date-formatter.service";
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/consts";
import {HijriGregorianDatepickerComponent} from "@shared/modules/hijri-gregorian-datepicker/datepicker/hijri-gregorian-datepicker.component";

@Component({
  selector: 'hijri-gregorian-range-picker',
  templateUrl: './hijri-gregorian-range-picker.component.html',
  styleUrls: ['./hijri-gregorian-range-picker.component.scss']
})
export class HijriGregorianRangePickerComponent implements OnInit {

  dateString: string;

  @Input() selectedDateType: DateType;
  @Input() selectedFromDate: NgbDateStruct;
  @Input() selectedToDate: NgbDateStruct;

  @Output() selectedFromDateChange: EventEmitter<Date> = new EventEmitter();
  @Output() selectedToDateChange: EventEmitter<Date> = new EventEmitter();
  @Output() selectedDateTypeChange: EventEmitter<DateType> = new EventEmitter();

  @Input() minHijri: NgbDateStruct;
  @Input() maxHijri: NgbDateStruct;
  @Input() minGreg: NgbDateStruct;
  @Input() maxGreg: NgbDateStruct;

  @ViewChild('fromDatePicker') fromDatePicker: HijriGregorianDatepickerComponent;
  @ViewChild('toDatePicker') toDatePicker: HijriGregorianDatepickerComponent;

  constructor(private dateFormatterService: DateFormatterService) {
  }

  ngOnInit(): void {
    this.selectedDateType = DateType.Gregorian;
  }

  onFromDateChange(event) {
    if (this.fromDatePicker.selectedDateType == DateType.Gregorian) {
      this.selectedDateType = DateType.Gregorian;
      this.toDatePicker.gregClick()
    } else {
      this.selectedDateType = DateType.Hijri;
      this.toDatePicker.hijriClick();
    }

    if (event) {
      let dateStruct = this.fromDatePicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : this.dateFormatterService.toGregorian(event);
      let dateStructGreg = this.fromDatePicker.selectedDateType == DateType.Gregorian ? event : this.dateFormatterService.toGregorian(event);
      this.dateString = this.dateFormatterService.toString(dateStruct);

      this.selectedFromDateChange.emit(this.dateFormatterService.toDate(dateStructGreg));

      // If toDate exceed fromDate then set toDate to the selected fromDate
      if (this.selectedToDate && this.dateFormatterService.toDate(this.selectedToDate) < this.dateFormatterService.toDate(event)) {
        this.selectedToDate = event;
      }

    } else {

      this.selectedFromDateChange.emit(null);

    }
  }

  onToDateChange(event) {
    if (this.toDatePicker.selectedDateType == DateType.Gregorian) {
      this.selectedDateType = DateType.Gregorian;
      this.fromDatePicker.gregClick();
    } else {
      this.selectedDateType = DateType.Hijri;
      this.fromDatePicker.hijriClick();
    }

    if (event) {
      let dateStruct = this.toDatePicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : this.dateFormatterService.toGregorian(event);
      let dateStructGreg = this.toDatePicker.selectedDateType == DateType.Gregorian ? event : this.dateFormatterService.toGregorian(event);
      this.dateString = this.dateFormatterService.toString(dateStruct);

      this.selectedToDateChange.emit(this.dateFormatterService.toDate(dateStructGreg));

      // If toDate exceed fromDate then set fromDate to the selected toDate
      if (this.selectedFromDate && this.dateFormatterService.toDate(this.selectedFromDate) > this.dateFormatterService.toDate(event)) {
        this.selectedFromDate = event;
      }

    } else {
      this.selectedToDateChange.emit(null);
    }
  }

  onDateTypeChange($event) {
    this.selectedDateTypeChange.emit($event);
  }

}
