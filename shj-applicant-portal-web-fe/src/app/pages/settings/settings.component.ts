import {Component, OnInit} from '@angular/core';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {AuthenticationService, CardService, UserService} from "@core/services";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DccValidators} from "@shared/validators";
import {COUNTRY} from "@model/enum/country_code";
import {Observable, OperatorFunction} from "rxjs";
import {debounceTime, map} from "rxjs/operators";
import {UserContacts} from "@model/UserContacts.model";
import {Router} from "@angular/router";
import {User} from "@shared/model";

import {CountryISO, PhoneNumberFormat, SearchCountryField} from 'ngx-intl-tel-input';
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  separateDialCode = false;
  SearchCountryField = SearchCountryField;
  CountryISO = CountryISO;
  PhoneNumberFormat = PhoneNumberFormat;

  closeResult = '';

  seasons: number [] = [];
  applicantRituals: ApplicantRitualLite [] = [];
  selectedSeason: number;
  selectedApplicantRitual: ApplicantRitualLite;
  ritualTypes: Lookup[] = [];
  enableEditRitual = false;
  enableEditLanguage = true;
  selectedLanguage = "";
  contactsForm: FormGroup;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;
  userCountry: any;
  COUNTRY = COUNTRY;
  userContacts: UserContacts = new UserContacts();
  selectedCountryCode = "ae";
  formattedCountryDial: any;

  constructor(private modalService: NgbModal,
              private userService: UserService,
              private cardService: CardService,
              private i18nService: I18nService,
              private toastr: ToastService,
              private formBuilder: FormBuilder,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private lookupsService: LookupService,
              private router: Router) {
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnInit() {
    this.userService.find(this.authenticationService.currentUser?.id).subscribe(data => {
      if (data && data.id) {

        this.contactsForm.disable();
        this.contactsForm.controls['email'].setValue(data.email);
        this.contactsForm.controls['mobileNumber'].setValue(data.mobileNumber);
        this.originalMobileNo = data.mobileNumber
        this.originalEmail = data.email;
        this.originalCountryCode = data.countryCode.toUpperCase();

        this.userCountry = COUNTRY.filter(function (c) {
          return data.countryCode.toUpperCase() == c.code;
        });
        this.contactsForm.controls["countryPhonePrefix"].setValue({dial_code: this.userCountry[0].dial_code});

        this.selectedCountryCode = data.countryCode.toLowerCase();
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));

      }
    });

    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });

    this.selectedSeason = JSON.parse((localStorage.getItem('selectedSeason')));
    this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    this.getRitualSeason();
    this.createForm();
  }


  search: OperatorFunction<string, readonly { dial_code, name, code }[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      map(term => term === '' ? this.COUNTRY
        // : this.statesWithFlags.filter(v => v.name.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
        : this.COUNTRY.filter(v => v.dial_code.toLowerCase().indexOf(term.toLowerCase()) > -1))
    )

  formatter = (x: { dial_code: string }) => x.dial_code;

  public onFocus(e: Event): void {
    e.stopPropagation();
    setTimeout(() => {
      const inputEvent: Event = new Event('input');
      e.target.dispatchEvent(inputEvent);
    }, 0);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  updateUserLanguage() {
    this.enableEditLanguage = true;
    if (this.selectedLanguage != "" && this.selectedLanguage != this.currentLanguage) {
      this.setLanguage(this.selectedLanguage);
      this.userService.updatePreferredLang(this.selectedLanguage.startsWith('ar') ? "ar" : "en").subscribe(response => {
        if (response && response.errors) {
          this.toastr.warning(this.translate.instant("general.dialog_error_text"), this.translate.instant("general.dialog_edit_title"));

        } else {
          this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));

        }

      });
    }
  }

  private createForm() {

    this.contactsForm = this.formBuilder.group({
      mobileNumber: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
      email: ['', [DccValidators.email, Validators.required]],
      countryPhonePrefix: [{dial_code: ''}, [Validators.required]]
    });


  }

  getRitualSeason() {
    this.userService.getApplicantSeason().subscribe(seasons => {
      console.log('seasons', seasons);
      this.seasons = seasons;
      if (this.seasons.length > 0) {
        console.log("this.selectedSeasonvv", this.selectedSeason)
        if (this.selectedSeason == null) {
          this.selectedSeason = this.seasons[0];
        }

        localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
        this.getApplicantRitualLiteBySeason(true);
      }
    })
  }

  getApplicantRitualLiteBySeason(firstCall: boolean) {

    if (this.selectedSeason) {
      this.applicantRituals = [];
      this.userService.getApplicantRitualLiteBySeason(this.selectedSeason).subscribe(applicantRituals => {
        this.applicantRituals = applicantRituals;
        if (this.applicantRituals.length > 0) {

          if (firstCall == true && this.selectedApplicantRitual != null) {
            let curSelected = this.applicantRituals.filter(ar => ar.id === this.selectedApplicantRitual.id);
            if (curSelected.length > 0) {
              this.changeSelectedApplicantRitual(this.selectedApplicantRitual);
            } else {
              this.changeSelectedApplicantRitual(this.applicantRituals[0]);
            }
          } else {
            this.changeSelectedApplicantRitual(this.applicantRituals[0]);
          }
        }
      });
    }
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.contactsForm?.controls;
  }

  onSeasonChange(selectedSeason: number) {
    this.selectedSeason = selectedSeason;
    this.getApplicantRitualLiteBySeason(false);
  }

  changeSelectedApplicantRitual(applicantRitual: ApplicantRitualLite) {
    this.selectedApplicantRitual = applicantRitual;

  }

  selectedItem($event) {
    this.contactsForm.controls["countryPhonePrefix"].setValue($event.item.dial_code);
    this.selectedCountryCode = $event.item.code.toLowerCase();
  }

  onApplicantRitualChange(applicantRitualId: number) {
    let curSelected = this.applicantRituals.filter(ar => ar.id == applicantRitualId)[0];
    this.changeSelectedApplicantRitual(curSelected);
  }

  saveSelectedRitual() {
    localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
    localStorage.setItem('selectedApplicantRitual', JSON.stringify(this.selectedApplicantRitual));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitual);

    this.enableEditRitual = false;
  }

  editRitual() {
    this.enableEditRitual = true;
  }

  setContactsEnabled() {
    this.contactsForm.enable();

  }

  onSubmit() {
    // trigger all validations
    Object.keys(this.contactsForm.controls).forEach(field => {
      const control = this.contactsForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.contactsForm.invalid) {
      return;
    }

    if (this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value ||
      this.originalEmail != this.contactsForm.controls['email'].value || this.originalCountryCode != this.selectedCountryCode.toUpperCase()) {

      let reg1 = /\+/gi;
      let reg2 = /\-/gi;
      this.userContacts.countryCode = this.selectedCountryCode.toUpperCase();
      this.userContacts.email = this.contactsForm.controls['email'].value;
      this.userContacts.mobileNumber = this.contactsForm.controls['mobileNumber'].value;
      this.formattedCountryDial = this.contactsForm.controls['countryPhonePrefix'].value.dial_code.replace(reg1, "00").replace(reg2, "");
      this.userContacts.countryPhonePrefix = this.formattedCountryDial;


      this.userService.generateOTPForEditContact(this.userContacts).subscribe(response => {
        if (!response) {
          this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("general.dialog_edit_title"));
        } else {
          let user: User = new User();

          user.otpExpiryMinutes = response.otpExpiryMinutes;
          user.maskedMobileNumber = response.mobileNumber;

          this.authenticationService.updateOtpSubject({
            user: user,
            actionType: "/settings",
            editContacts: this.userContacts
          });

          this.router.navigate(['/edit/contacts/otp'], {replaceUrl: true});


        }
      })

    } else {
      this.contactsForm.disable();
    }
  }

}
