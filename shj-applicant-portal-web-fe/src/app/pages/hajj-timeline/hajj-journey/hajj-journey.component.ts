import {Component, OnInit} from '@angular/core';
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {RitualTimelineService} from "@core/services/timeline/ritual-timeline.service";
import {UserService} from "@core/services";
import * as moment_ from 'moment-hijri';
import {LookupService} from "@core/utilities/lookup.service";
import {Lookup} from "@model/lookup.model";
import {hijriMonth} from "@shared/helpers/hijri-month.helper";
import {ApplicantRitualPackage} from "@model/applicant-ritual-package.model";
import {ActivatedRoute, Router} from "@angular/router";

const momentHijri = moment_;

@Component({
  selector: 'app-hajj-journey',
  templateUrl: './hajj-journey.component.html',
  styleUrls: ['./hajj-journey.component.scss']
})
export class HajjJourneyComponent implements OnInit {

  private ritualSteps: CompanyRitualMainDataStep[] = [];
  ritualStepsMap: { key: Date, value: CompanyRitualMainDataStep[], isActive: boolean, day: string, month: string; }[];
  selectedApplicantRitualPackage: ApplicantRitualPackage;
  lookupService: LookupService;
  ritualStepsLookups: Lookup[] = [];

  constructor(private ritualTimelineService: RitualTimelineService, private userService: UserService, lookupService: LookupService,  private route: ActivatedRoute,
              private router: Router) {
    this.lookupService = lookupService;
  }

  ngOnInit(): void {
    this.loadLookups();
    this.selectedApplicantRitualPackage = JSON.parse(localStorage.getItem('selectedApplicantRitualPackage'));

    if (!this.selectedApplicantRitualPackage) {
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
    this.ritualTimelineService.loadRitualSteps(this.selectedApplicantRitualPackage?.companyRitualSeasonId).subscribe(
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
    this.userService.selectedApplicantRitualPackage.subscribe(season => {
      this.selectedApplicantRitualPackage = season;
      this.loadRitualSteps();
    })
  }

  showDetails(stepCode: string, stepDay: any , stepMonth: any){


    let description = this.lookupService.localizedSummary(this.ritualStepsLookups, stepCode)
    let step = {stepDescription:description, stepDay:stepDay , stepMonth:stepMonth }
      this.ritualTimelineService.getRitualStepSubject(step)
    this.router.navigate(['/hajj-journey/details'], {replaceUrl: true});
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
