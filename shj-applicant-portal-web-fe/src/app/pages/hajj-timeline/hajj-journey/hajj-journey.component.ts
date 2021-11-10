import {Component, OnInit} from '@angular/core';
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {RitualTimelineService} from "@core/services/timeline/ritual-timeline.service";
import {UserService} from "@core/services";
import * as moment_ from 'moment-hijri';
import {LookupService} from "@core/utilities/lookup.service";
import {Lookup} from "@model/lookup.model";
import {hijriMonth} from "@shared/helpers/hijri-month.helper";
import {CompanyRitualSeasonLite} from "@model/company-ritual-season-lite.model";

const momentHijri = moment_;

@Component({
  selector: 'app-hajj-journey',
  templateUrl: './hajj-journey.component.html',
  styleUrls: ['./hajj-journey.component.scss']
})
export class HajjJourneyComponent implements OnInit {

  private ritualSteps: CompanyRitualMainDataStep[] = [];
  ritualStepsMap: { key: Date, value: CompanyRitualMainDataStep[], isActive: boolean, day: string, month: string; }[];
  selectedRitualSeason: CompanyRitualSeasonLite;
  lookupService: LookupService;
  ritualStepsLookups: Lookup[] = [];

  constructor(private ritualTimelineService: RitualTimelineService, private userService: UserService, lookupService: LookupService) {
    this.lookupService = lookupService;
  }

  ngOnInit(): void {
    this.loadLookups();
    this.selectedRitualSeason = JSON.parse(localStorage.getItem('selectedRitualSeason'));

    if (!this.selectedRitualSeason) {
      this.loadApplicantRitualFromService();
    } else {
      this.loadRitualSteps();
    }

  }

  handleIsActiveStep() {
    this.ritualStepsMap.forEach((stepGroups, index) => {
      const dateFor = this.ritualStepsMap[index + 1] === undefined ? new Date().getUTCDate() + 1 : new Date(this.ritualStepsMap[index + 1].key).getUTCDate();
      if (new Date().getUTCDate() >= new Date(stepGroups?.key).getUTCDate() &&
        new Date().getUTCDate() < dateFor) {
        stepGroups.isActive = true;
      }
    })
  }

  loadRitualSteps() {
    this.ritualTimelineService.loadRitualSteps(this.selectedRitualSeason.id).subscribe(
      result => {
        this.ritualSteps = result;
        this.ritualStepsMap = groupByArray(this.ritualSteps);
        this.handleIsActiveStep();
      }
    );
  }

  loadLookups() {
    this.ritualTimelineService.loadRitualStepsLookups().subscribe(result => {
      this.ritualStepsLookups = result;
    });
  }

  loadApplicantRitualFromService() {
    this.userService.selectedApplicantRitual.subscribe(season => {
      this.selectedRitualSeason = season;
      this.loadRitualSteps();
    })
  }
}

function groupByArray(xs: CompanyRitualMainDataStep[]) {
  return xs.reduce(function (arr, x) {
    let time: Date = x.time;
    let el = arr.find(r => new Date(r.key).getUTCDate() === new Date(time).getUTCDate());
    if (el) {
      el.value.push(x);
    } else {
      arr.push({
        key: time,
        value: [x],
        isActive: false,
        day: momentHijri(time).iDate(),
        month: hijriMonth[momentHijri(time).iMonth() + 1]
      });
    }
    return arr;
  }, []);
}
