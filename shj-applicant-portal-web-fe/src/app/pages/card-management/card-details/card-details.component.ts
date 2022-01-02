import {Component, OnDestroy, OnInit} from '@angular/core';
import {Card} from "@model/card.model";
import {TranslateService} from "@ngx-translate/core";
import {I18nService} from "@dcc-commons-ng/services";
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
import {ApplicantPackageDetails} from "@model/applicant-package-details.model";
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {GroupLeader} from "@model/group-leader.model";
import {CardStatus} from "@model/enum/card-status.enum";
import {DigitalIdStatus} from "@model/enum/digital-id-status.enum";

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.scss']
})
export class CardDetailsComponent implements OnInit, OnDestroy {

  card: Card;
  applicant: ApplicantMainData;
  healthDetails: ApplicantHealth;
  tafweejDetails: CompanyRitualMainDataStep[];
  groupLeaders: GroupLeader[];
  url: any = 'assets/images/default-avatar.svg';
  applicantStatuses: Lookup[] = [];
  applicantPackage: ApplicantPackageDetails = null;
  loading = true
  ritualTypes: Lookup[] = [];
  housingCategories: Lookup[];
  housingTypes: Lookup[];
  packageTypes: Lookup[];
  housingSites: Lookup[];
  transportationTypes: Lookup[];
  relativeRelationships: Lookup[] = [];
  countries: CountryLookup[] = [];
  healthSpecialNeeds: Lookup[] = [];
  maritalStatuses: Lookup[] = [];
  ritualStepsLabels: Lookup[];
  groupLeaderTitle: Lookup[];
  cardStatuses: Lookup[];
  immunizations: Lookup[];
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
              private userService: UserService) {
  }


  ngOnInit(): void {
    this.loadLookups();
    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual => {
      this.selectedApplicantRitual = selectedApplicantRitual;
      this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedRitualSeason'));
      this.loadUserDetails();
    });
  }

  loadUserDetails() {
    if (this.selectedApplicantRitual) {
      this.loading = true;
      this.cardService.findMainProfile(this.selectedApplicantRitual?.id).subscribe(data => {
        if (data) {
          this.applicant = data;
        } else {
          this.toastr.error(this.translate.instant('general.route_item_not_found'),
            this.translate.instant('general.dialog_error_title'));
        }
        this.loading = false;
      });

      this.applicantPackage = null;
    }
  }

  loadTafweejDetails() {
    this.cardService.findTafweejDetails(this.selectedApplicantRitual.id).subscribe(data => {
      if (data) {
        this.tafweejDetails = data;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });
  }

  loadGroupLeaders() {
    this.cardService.findGroupLeadersDetails(this.selectedApplicantRitual.id).subscribe(data => {
      if (data) {
        this.groupLeaders = data;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });
  }

  loadUserPackageDetails() {
    if (this.applicantPackage == null) {

      this.cardService.findPackageDetails(this.selectedApplicantRitual.id).subscribe(data => {
        if (data) {
          this.applicantPackage = data;
        } else {
          this.toastr.error(this.translate.instant('general.route_item_not_found'),
            this.translate.instant('general.dialog_error_title'));
        }

      });
    }
  }

  loadHealthDetails() {
    if (this.healthDetails == null) {
      this.cardService.findHealthDetails(this.selectedApplicantRitual?.id).subscribe(data => {
        if (data) {
          this.healthDetails = data;
        } else {
          this.toastr.error(this.translate.instant('general.route_item_not_found'),
            this.translate.instant('general.dialog_error_title'));
        }
      });
    }
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

    this.cardService.findHousingTypes().subscribe(result => {
      this.housingTypes = result;
    });

    this.cardService.findHousingCategories().subscribe(result => {
      this.housingCategories = result;
    });

    this.cardService.findPackageTypes().subscribe(result => {
      this.packageTypes = result;
    });
    this.cardService.findRitualStepsLabels().subscribe(result => {
      this.ritualStepsLabels = result;
    });
    this.cardService.findGroupLeadersTitle().subscribe(result => {
      this.groupLeaderTitle = result;
    });
    this.cardService.findHousingSites().subscribe(result => {
      this.housingSites = result;
    });

    this.cardService.findTransportationTypes().subscribe(result => {
      this.transportationTypes = result;
    });

    this.cardService.findCardStatuses().subscribe(result => {
      this.cardStatuses = result;
    });

    this.cardService.findDigitalIdStatuses().subscribe(result => {
      this.applicantStatuses = result;
    });

    this.cardService.findHealthImmunizations().subscribe(result => {
      this.immunizations = result;
    });
  }

  getCardStatus(code: string): string {
    if (code !== CardStatus.SUSPENDED && code != CardStatus.CANCELLED) {
      return this.translate.instant('card-management.status_active');
    }
    return this.lookupService().localizedLabel(this.cardStatuses, code);
  }

  /**
   * Returns the css class for the given status
   *
   * @param status the current card status
   */
  buildCardStatusClass(status: any): string {
    switch (status) {
      case CardStatus.ACTIVE:
        return "done";
      case CardStatus.SUSPENDED:
        return "Suspended";
      case CardStatus.CANCELLED:
        return "new";
      default:
        return "done";
    }
  }

  buildDigitalIdClass(status: any): string {
    switch (status) {
      case DigitalIdStatus.VALID:
        return "done";
      case DigitalIdStatus.INVALID:
        return "Suspended";
      default:
        return "done";
    }
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  ngOnDestroy(): void {
    this.loading = true;
  }
}
