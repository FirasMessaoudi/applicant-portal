import {Component, OnInit} from '@angular/core';
import {Card} from "@model/card.model";
import {TranslateService} from "@ngx-translate/core";
import {I18nService} from "@dcc-commons-ng/services";
import {PackageCatering} from "@model/package-catering.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CardService, UserService} from "@core/services";
import {ToastService} from "@shared/components/toast";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";
import {CountryLookup} from "@model/country-lookup.model";
import {ApplicantMainData} from "@model/applicant-main-data.model";
import {Language} from "@model/enum/language.enum";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {ApplicantHealth} from "@model/applicant-health.model";

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.scss']
})
export class CardDetailsComponent implements OnInit {

  card: Card;
  applicant: ApplicantMainData;
  healthDetails: ApplicantHealth;
  url: any = 'assets/images/default-avatar.svg';
  //TODO: to be deleted after wiring the backend to the frontend
  hamlahPackage: any;

  ritualTypes: Lookup[] = [];
  relativeRelationships: Lookup[] = [];
  countries: CountryLookup[] = [];
  healthSpecialNeeds: Lookup[] = [];
  maritalStatuses: Lookup[] = [];
  languageNativeName = Language;

  selectedApplicantRitual: ApplicantRitualLite;

  activeId = 1;
  tabsHeader = [
    "card-management.main_details",
    "card-management.hamlah_details",
    "card-management.health_details",
    "card-management.tafweej_details",
    "card-management.motawef_details"
  ]

  constructor(private route: ActivatedRoute,
              private router: Router,
              private toastr: ToastService,
              private cardService: CardService,
              private translate: TranslateService,
              private i18nService: I18nService,
              private lookupsService: LookupService,
              private userService: UserService
  ) {
  }


  ngOnInit(): void {

    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual => {
      this.selectedApplicantRitual = selectedApplicantRitual;
    });

    this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    this.loadLookups();
    this.cardService.findMainProfile(this.selectedApplicantRitual.id).subscribe(data => {
      if (data) {
        this.applicant = data;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });

    this.cardService.findHealthDetails(this.selectedApplicantRitual?.id).subscribe(data => {
      if (data) {
        this.healthDetails = data;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });

    //TODO: dummy data
    this.hamlahPackage = {
      "id": 1,
      "typeCode": {"id": 1, "code": null, "label": "VIP", "lang": null},
      "hamlahId": 1,
      "campId": 1,
      "price": 6500,
      "departureCity": "Mombai",
      "countryId": 1,
      "packageHousings": [{
        "id": 1,
        "type": "مزدلفة",
        "code": "25475",
        "labelAr": "مخيم مشاعل النور",
        "labelEn": "",
        "validityStart": "01-04-2021",
        "validityEnd": "02-04-2021",
        "addressAr": "مزدلفة بجوار محطة القطار",
        "addressEn": "",
        "isDefault": true,
        "packageCaterings": [{"id": 1, "type": "غداء", "meal": "", "descriptionAr": "بوفيه مفتوح", "descriptionEn": ""}]
      }],
      "packageTransportations": [{
        "id": 1,
        "type": "باص",
        "validityStart": "03-04-2021",
        "validityEnd": "04-04-2021",
        "locationFrom": "منى",
        "locationTo": "مزدلفة",
        "vehicleNumber": "ب ح ج 259"
      }]
    };
  }

  loadLookups() {
    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });
    this.cardService.findRelativeRelationships().subscribe(result => {
      this.relativeRelationships = result;
    });
    this.cardService.findCountries().subscribe(result => {
      this.countries = result;
    });
    this.cardService.findHealthSpecialNeeds().subscribe(result => {
      this.healthSpecialNeeds = result;
    });
    this.cardService.findMaritalStatuses().subscribe(result => {
      this.maritalStatuses = result;
    });
  }

  // goToList() {
  //   this.router.navigate(['/cards/list']);
  // }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  packageCaterings(): PackageCatering[] {
    let packageCaterings: PackageCatering[] = [];
    //TODO: to be refactored after wiring the backend to the frontend
    this.hamlahPackage.packageHousings.forEach(housing => {
      packageCaterings = packageCaterings.concat(housing.packageCaterings);
    });
    return packageCaterings;
  }

}
