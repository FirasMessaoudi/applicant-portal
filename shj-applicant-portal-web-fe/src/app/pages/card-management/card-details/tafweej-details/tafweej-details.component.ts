import {Component, Input, OnInit} from '@angular/core';
import {Lookup} from "@model/lookup.model";
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {LookupService} from "@core/utilities/lookup.service";
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-tafweej-details',
  templateUrl: './tafweej-details.component.html',
  styleUrls: ['./tafweej-details.component.scss']
})
export class TafweejDetailsComponent implements OnInit {
  @Input()
  ritualStepsLabels: Lookup [];
  @Input()
  companyRitualSteps: CompanyRitualMainDataStep[];
  constructor(private lookupsService: LookupService,
              private i18nService: I18nService
  ) { }

  ngOnInit(): void {
  }
  lookupService(): LookupService {
    return this.lookupsService;
  }
  get currentLanguage(): string {
    return this.i18nService.language;
  }
}
