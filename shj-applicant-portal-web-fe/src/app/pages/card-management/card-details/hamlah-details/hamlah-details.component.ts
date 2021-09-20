import {Component, Input, OnInit} from '@angular/core';
import {ApplicantPackageDetails} from "@model/applicant-package-details.model";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";

@Component({
  selector: 'app-hamlah-details',
  templateUrl: './hamlah-details.component.html',
  styleUrls: ['./hamlah-details.component.scss']
})
export class HamlahDetailsComponent implements OnInit {

  @Input()
  applicantPackage: ApplicantPackageDetails =null;

  @Input()
  housingCategories: Lookup[];
  @Input()
  housingTypes: Lookup[];
  @Input()
  packageTypes: Lookup[];
  @Input()
  housingSites: Lookup[];

  @Input()
  currentLanguage: string;

  @Input()
  transportationTypes: Lookup[];

  constructor(private lookupsService: LookupService) {
  }

  ngOnInit(): void {
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

}
