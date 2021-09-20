import {Component, OnInit} from '@angular/core';
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {RitualTimelineService} from "@core/services/timeline/ritual-timeline.service";
import {UserService} from "@core/services";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import * as moment_ from 'moment-hijri';
import {LookupService} from "@core/utilities/lookup.service";
import {Lookup} from "@model/lookup.model";

const momentHijri = moment_;

@Component({
  selector: 'app-hajj-journey',
  templateUrl: './hajj-journey.component.html',
  styleUrls: ['./hajj-journey.component.scss']
})
export class HajjJourneyComponent implements OnInit {
  ritualType = ''
  ritualTypesLookups: Lookup[]
  private ritualSteps: CompanyRitualMainDataStep[] = []
  ritualStepsMap: { key: Date, value: CompanyRitualMainDataStep[], isActive: boolean, day: string, month: string }[]
  selectedApplicantRitual: ApplicantRitualLite
  lookupService: LookupService
  ritualStepsLookups: Lookup[] = []

  constructor(private ritualTimelineService: RitualTimelineService, private userService: UserService, lookupService: LookupService) {
    this.lookupService = lookupService
  }

  ngOnInit(): void {
    this.loadLookups()
    this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));
    this.ritualType = this.selectedApplicantRitual.typeCode

    if (this.selectedApplicantRitual === null) {
      this.loadApplicantRitualFromService()
    } else {
      this.loadRitualSteps()
    }

  }

  handleIsActiveStep() {
    this.ritualStepsMap.forEach((stepGroups, index) => {
      const dateFor = this.ritualStepsMap[index + 1] === undefined ? new Date().getDate() + 1 : this.ritualStepsMap[index + 1].key.getDate()
      if (new Date().getDate() >= stepGroups?.key.getDate() &&
        new Date().getDate() < dateFor) {
        stepGroups.isActive = true;
      }
    })
  }

  loadRitualSteps() {
    this.ritualTimelineService.loadRitualSteps(this.selectedApplicantRitual.id).subscribe(
      result => {
        this.ritualSteps = result
        this.ritualStepsMap = groupByArray(this.ritualSteps)
        this.handleIsActiveStep()
      }
    );
  }

  loadLookups() {
    this.ritualTimelineService.loadRitualStepsLookups().subscribe(result => {
      this.ritualStepsLookups = result;
    });

    this.ritualTimelineService.loadRitualTypes().subscribe(result => {
      this.ritualTypesLookups = result;
    });
  }

  loadApplicantRitualFromService() {
    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual => {
      this.ritualType = selectedApplicantRitual.name
      this.selectedApplicantRitual = selectedApplicantRitual
      this.loadRitualSteps()
    })
  }
}

function groupByArray(xs: CompanyRitualMainDataStep[]) {
  return xs.reduce(function (arr, x) {
    let time: Date = x.time
    let el = arr.find((r) => {
      return r.key.getDate() === time.getDate()
    });
    if (el) {
      el.value.push(x);
    } else {
      arr.push({
        key: time,
        value: [x],
        isActive: false,
        day: momentHijri(time).iDate(),
        month: momentHijri(time).iMonth()
      });
    }
    return arr;
  }, []);
}
