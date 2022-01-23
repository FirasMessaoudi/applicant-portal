import {Component, Input, OnInit} from '@angular/core';
import {CardService, UserService} from "@core/services";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {I18nService} from "@dcc-commons-ng/services";
import {LookupService} from "@core/utilities/lookup.service";
import {ApplicantRitualCard} from "@model/applicant-ritual-card";
import {CountryLookup} from "@model/country-lookup.model";
import {Lookup} from "@model/lookup.model";
import {ApplicantRitualPackage} from "@model/applicant-ritual-package.model";

@Component({
  selector: 'app-badge',
  templateUrl: './badge.component.html',
  styleUrls: ['./badge.component.scss']
})
export class BadgeComponent implements OnInit {

  selectedApplicantRitualPackage: ApplicantRitualPackage;
  cardDetails: ApplicantRitualCard;
  countries: CountryLookup[] = [];
  ritualTypes: Lookup[] = [];
  url: any = 'assets/images/default-avatar.svg';
  @Input() uin = '';
  @Input() cardStatus = '';

  constructor(private toastr: ToastService,
              private cardService: CardService,
              private translate: TranslateService,
              private i18nService: I18nService,
              private lookupsService: LookupService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.loadLookups();

    this.userService.selectedApplicantRitualPackage.subscribe(selectedApplicantRitualPackage => {
      this.selectedApplicantRitualPackage = selectedApplicantRitualPackage;
    });

    this.selectedApplicantRitualPackage = JSON.parse(localStorage.getItem('selectedApplicantRitualPackage'));

    this.cardService.findCardDetails(this.selectedApplicantRitualPackage?.applicantPackageId).subscribe(data => {
      if (data) {
        this.cardDetails = data;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      this.countries = result;
    });
    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });
  }

  getLookupLabel(data: any[], code: string, lang: string): string {
    return data.find(country => code === country.code  && lang.toUpperCase() === country.lang.toUpperCase())?.label;
  }
}
