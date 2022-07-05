import {Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DateType} from './consts';
import {DateFormatterService} from './date-formatter.service';

import * as momentjs from 'moment';
import * as moment_ from 'moment-hijri';

const moment = momentjs;

const momentHijri = moment_;

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'hijri-gregorian-datepicker',
  templateUrl: './hijri-gregorian-datepicker.component.html',
  styleUrls: ['./hijri-gregorian-datepicker.component.scss']
})
export class HijriGregorianDatepickerComponent implements OnInit {

  @ViewChild('d') datePicker: any;

  @Input() selectedDateType: DateType;
  @Input() selectedDate: NgbDateStruct;
  @Output() selectedDateChange: EventEmitter<NgbDateStruct> = new EventEmitter();
  @Output() selectedDateTypeChange: EventEmitter<DateType> = new EventEmitter();

  @Input() name: string;

  @Input() label: string;
  @Input() showLabel = true;

  @Input() buttonClass: string = 'dcc-primary';

  @Input() readonly = false;
  @Input() isRequired = false;
  @Input() disabled = false;

  @Input() minHijri: NgbDateStruct;
  @Input() maxHijri: NgbDateStruct;
  @Input() minGreg: NgbDateStruct;
  @Input() maxGreg: NgbDateStruct;

  @Input() hijriLabel: string;
  @Input() GregLabel: string;

  @Input() placeHolder: string;

  @Input() hideToggleButton = false;

  get DateType() {
    return DateType;
  }

  constructor(private dateFormatterService: DateFormatterService) { }

  ngOnInit() {
    if (!this.selectedDateType) {
      this.selectedDateType = DateType.Hijri;
    }
  }

  close() {
    this.datePicker.close();
  }

  onBlur(event){
    if(!this.selectedDate && this.isRequired){
      this.selectedDate = undefined;
      this.selectedDateChange.emit(null);
    }

  }

  clear() {
    this.selectedDate = undefined;
    this.close();
    this.selectedDateChange.emit(null);
  }
  getSelectedDate(): string {

    let formattedDate = this.dateFormatterService.toString(this.selectedDate);

    if (this.selectedDateType == DateType.Hijri) {
      return momentHijri(formattedDate, 'iDD/iMM/iYYYY').locale('en').format();
    }

    if (this.selectedDateType == DateType.Gregorian) {
      return moment(formattedDate, 'DD/MM/YYYY').locale('en').format();
    }
  }

  dateSelected() {
    this.selectedDateChange.emit(this.selectedDate);
  }

  hijriClick() {
    if (this.selectedDateType == DateType.Hijri) {
      return;
    }
    this.selectedDateType = DateType.Hijri;
    //to hijri
    this.selectedDate = this.dateFormatterService.toHijri(this.selectedDate);
    this.selectedDateChange.emit(this.selectedDate);
    this.selectedDateTypeChange.emit(this.selectedDateType);
  }
  gregClick() {
    if (this.selectedDateType == DateType.Gregorian) {
      return;
    }
    this.selectedDateType = DateType.Gregorian;
    //to Gregorian
    this.selectedDate = this.dateFormatterService.toGregorian(this.selectedDate);
    this.selectedDateChange.emit(this.selectedDate);
    this.selectedDateTypeChange.emit(this.selectedDateType);
  }
}
