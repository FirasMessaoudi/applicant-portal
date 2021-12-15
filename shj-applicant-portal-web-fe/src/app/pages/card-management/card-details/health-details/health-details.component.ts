import {Component, Input, OnInit} from '@angular/core';
import {ApplicantHealth} from "@model/applicant-health.model";
import {LookupService} from "@core/utilities/lookup.service";
import {Lookup} from "@model/lookup.model";

@Component({
  selector: 'app-health-details',
  templateUrl: './health-details.component.html',
  styleUrls: ['./health-details.component.scss']
})
export class HealthDetailsComponent implements OnInit {
  @Input()
  healthDetails: ApplicantHealth;
  @Input()
  healthSpecialNeeds: Lookup[];
  @Input()
  immunizations: Lookup[];
  @Input()
  currentLanguage: string;

  constructor(
    private lookupsService: LookupService,
  ) {
  }

  ngOnInit(): void {
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }
}
