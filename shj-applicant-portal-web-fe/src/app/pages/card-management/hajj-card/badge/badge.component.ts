import {Component, OnInit} from '@angular/core';
import {CardService, UserService} from "@core/services";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {I18nService} from "@dcc-commons-ng/services";
import {LookupService} from "@core/utilities/lookup.service";
import {ApplicantRitualCard} from "@model/applicant-ritual-card";
import {CountryLookup} from "@model/country-lookup.model";
import {Lookup} from "@model/lookup.model";

@Component({
  selector: 'app-badge',
  templateUrl: './badge.component.html',
  styleUrls: ['./badge.component.scss']
})
export class BadgeComponent implements OnInit {

  selectedApplicantRitual: ApplicantRitualLite;
  cardDetails: ApplicantRitualCard;
  countries: CountryLookup[] = [];
  ritualTypes: Lookup[] = [];
  url: any = 'assets/images/default-avatar.svg';

  constructor(private toastr: ToastService,
              private cardService: CardService,
              private translate: TranslateService,
              private i18nService: I18nService,
              private lookupsService: LookupService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.loadLookups();

    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual => {
      this.selectedApplicantRitual = selectedApplicantRitual;
    });

    this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    this.cardService.findCardDetails(this.selectedApplicantRitual?.id).subscribe(data => {
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

  lookupService(): LookupService {
    return this.lookupsService;
  }

  getLookupLabel(data: any[], code: string, lang: string) {
    return data.find(country => code === country.code  && lang.toUpperCase() === country.lang.toUpperCase()).label;
  }
}
