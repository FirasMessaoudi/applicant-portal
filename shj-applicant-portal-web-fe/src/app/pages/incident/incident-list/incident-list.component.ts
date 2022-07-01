import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { EAuthority, Page } from '@shared/model';
import { AuthenticationService } from '@core/services';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { ApplicantIncident } from '@model/applicant-incident.model';
import { Subscription } from 'rxjs';
import { IncidentService } from '@core/services/incident/incident.service';
import { Lookup } from '@model/lookup.model';
import { LookupService } from '@core/utilities/lookup.service';
import { I18nService } from '@dcc-commons-ng/services';
import { DatePipe } from '@angular/common';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/datepicker/consts";
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/datepicker/date-formatter.service";

@Component({
  selector: 'app-incident-list',
  templateUrl: './incident-list.component.html',
  styleUrls: ['./incident-list.component.scss'],
})
export class IncidentListComponent implements OnInit, OnDestroy {
  isSearchbarCollapsed = true;
  pageArray: Array<number>;
  incidents: ApplicantIncident[] = [];
  incidentTypes: Lookup[] = [];
  incidentStatuses: Lookup[] = [];
  listSubscription: Subscription;
  searchSubscription: Subscription;
  selectedDateType: DateType;
  todayGregorian: NgbDateStruct;
  todayHijri: NgbDateStruct;

  @ViewChild('picker') picker: any;

  constructor(
    private authenticationService: AuthenticationService,
    private incidentService: IncidentService,
    private formBuilder: FormBuilder,
    private lookupsService: LookupService,
    private i18nService: I18nService,
    private dateFormatterService: DateFormatterService
  ) {}

  ngOnInit(): void {
    this.selectedDateType = DateType.Gregorian;

    this.todayGregorian = this.dateFormatterService.todayGregorian();
    this.todayHijri = this.dateFormatterService.todayHijri();

    this.loadLookups();
    this.loadIncidentList();
  }

  ngOnDestroy() {
    if (this.listSubscription) {
      this.listSubscription.unsubscribe();
    }
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }

  loadLookups() {
    this.incidentService.findIncidentTypes().subscribe((result) => {
      this.incidentTypes = result;
    });
    this.incidentService.findIncidentStatuses().subscribe((result) => {
      this.incidentStatuses = result;
    });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  loadIncidentList() {
    this.listSubscription = this.incidentService
      .list()
      .subscribe((data) => {
        this.incidents = data;
      });
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  /**
   * Returns the css class for the given status
   *
   * @param status the current applicant incident status
   */
  buildStatusClass(status: any): string {
    switch (status) {
      case 'UNDER_PROCESSING':
        return 'ready';
      case 'RESOLVED':
        return 'done';
      case 'CLOSED':
        return 'warning';
      default:
        return 'new';
    }
  }

  patchValue(event: Date, c: AbstractControl) {
    c.setValue(event);
  }

  setSelectedDateType(event: DateType) {
    this.selectedDateType = event;
  }

  formatDate(date: Date): string {
    const datePipe = new DatePipe('en-US');
    if (this.selectedDateType === DateType.Hijri) {
      let hijriDate = this.dateFormatterService.toDate(
        this.dateFormatterService.toHijri(
          this.dateFormatterService.fromDate(date)
        )
      );
      return this.currentLanguage.startsWith('ar')
        ? datePipe.transform(hijriDate, 'yyyy/MM/dd')
        : datePipe.transform(hijriDate, 'dd/MM/yyyy');
    } else {
      return this.currentLanguage.startsWith('ar')
        ? datePipe.transform(date, 'yyyy/MM/dd')
        : datePipe.transform(date, 'dd/MM/yyyy');
    }
  }
}
