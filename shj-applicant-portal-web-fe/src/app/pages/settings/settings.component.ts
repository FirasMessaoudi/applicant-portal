import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgbDropdown, NgbModal, NgbTypeahead} from '@ng-bootstrap/ng-bootstrap';
import {AuthenticationService, CardService, UserService} from "@core/services";
import {LookupService} from "@core/utilities/lookup.service";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DccValidators} from "@shared/validators";
import {UserContacts} from "@model/UserContacts.model";
import {Router} from "@angular/router";
import {User} from "@shared/model";
import {I18nService} from "@dcc-commons-ng/services";
import {merge, Observable, Subject} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {Lookup} from "@model/lookup.model";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  seasons: number [] = [];
  applicantRituals: ApplicantRitualLite [] = [];
  selectedSeason: number;
  selectedApplicantRitual: ApplicantRitualLite;
  ritualTypes: Lookup[] = [];
  enableEditLanguage = false;
  selectedLanguage = "";
  contactsForm: FormGroup;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;
  userContacts: UserContacts = new UserContacts();
  countries: any;
  selectedCountryCode;
  selectedCountryPrefix: any;
  originalCountryPrefix: any;
  @ViewChild('instance')
  instance: NgbTypeahead;
  @ViewChild('elem')
  elem: ElementRef;
  @ViewChild('countryListDropdown')
  countryListDropdown: NgbDropdown;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  constructor(private modalService: NgbModal,
              private userService: UserService,
              private cardService: CardService,
              private i18nService: I18nService,
              private toastr: ToastService,
              private formBuilder: FormBuilder,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private lookupsService: LookupService,
              private elRef: ElementRef,
              private router: Router) {
  }

  ngOnInit() {
    this.loadLookups();
    this.selectedLanguage = this.currentLanguage;
    this.userService.find(this.authenticationService.currentUser?.id).subscribe(data => {
      if (data && data.id) {
        this.contactsForm.disable();
        this.contactsForm.controls['email'].setValue(data.email);
        this.contactsForm.controls['mobileNumber'].setValue(data.mobileNumber);
        this.originalMobileNo = data.mobileNumber
        this.originalEmail = data.email;
        this.originalCountryCode = data.countryCode.toLowerCase();
        this.selectedCountryCode = data.countryCode.toLowerCase();
        this.originalCountryPrefix = '+' + data.countryPhonePrefix;
        this.selectedCountryPrefix = '+' + data.countryPhonePrefix;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));
      }
    });

    this.createForm();
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
      this.userService.updatePreferredLang(this.selectedLanguage?.startsWith('ar') ? "ar" : "en").subscribe(response => {
        if (response && response.errors) {
          this.toastr.warning(this.translate.instant("general.dialog_error_text"), this.translate.instant("general.dialog_edit_title"));
        } else {
          this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));
        }
      });
    }
    this.enableEditLanguage = false;
  }

  cancelEditLanguage() {
    this.selectedLanguage = this.currentLanguage;
    this.enableEditLanguage = false;
  }

  private createForm() {
    this.contactsForm = this.formBuilder.group({
      mobileNumber: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [DccValidators.email, Validators.required]],
    });
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      result.forEach(c => c.countryPhonePrefix = '+' + c.countryPhonePrefix);
      this.countries = result;
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.contactsForm?.controls;
  }


  setContactsEnabled() {
    this.contactsForm.enable();
  }

  setContactsDisabled() {
    this.contactsForm.disable();
    this.contactsForm.controls['email'].setValue(this.originalEmail);
    this.contactsForm.controls['mobileNumber'].setValue(this.originalMobileNo);
    this.selectedCountryPrefix = this.originalCountryPrefix;
    this.selectedCountryCode = this.originalCountryCode;
  }

  onSubmit() {
    // trigger all validations
    Object.keys(this.contactsForm.controls).forEach(field => {
      const control = this.contactsForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.contactsForm.invalid) {

      console.log(this.contactsForm.controls['mobileNumber'].errors);
      return;
    }
    console.log(this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.replace(/\s/g, ""))
    if (this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.replace(/\s/g, "") ||
      this.originalEmail != this.contactsForm.controls['email'].value ||
      this.originalCountryCode.toUpperCase() != this.selectedCountryCode.toUpperCase()) {


      let reg1 = / /g;
      let reg2 = /\+/gi;
      let reg3 = /\-/gi;

      this.userContacts.countryCode = this.selectedCountryCode.toUpperCase();
      this.userContacts.email = this.contactsForm.controls['email'].value;
      this.userContacts.mobileNumber = this.contactsForm.controls['mobileNumber'].value.replace(reg1, "").replace(reg3, "");
      this.userContacts.countryPhonePrefix = this.selectedCountryPrefix.replace(reg2, "");

      console.log(this.userContacts);

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

  public openTypeahead(): void {
    // Dispatch event on input element that NgbTypeahead is bound to
    this.elem.nativeElement.dispatchEvent(new Event('input'));
    // Ensure input has focus so the user can start typing
    setTimeout(() => { // this will make the execution after the above boolean has changed
      this.elem.nativeElement.focus();
    }, 0);
  }

  search = (text$: Observable<string>) => {
    const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
    const inputFocus$ = this.focus$;
    return merge(text$, inputFocus$, clicksWithClosedPopup$).pipe(
      map(term => (term === '' ? this.countries.filter(c => c.lang.toLowerCase() === this.i18nService.language.substr(0, 2))
        : this.countries.filter(c => c.lang.toLowerCase() === this.i18nService.language.substr(0, 2)).filter(v => v.label.toLowerCase().indexOf(term.toLowerCase()) > -1 || v.countryPhonePrefix.toLowerCase().indexOf(term) > -1
        ))));
  }

  inputFormatter = (x: { countryPhonePrefix: string }) => x.countryPhonePrefix

  onSelect($event, input) {
    $event.preventDefault();
    this.selectedCountryPrefix = $event.item.countryPhonePrefix;
    this.selectedCountryCode = $event.item.code.toLowerCase();
    this.countryListDropdown.close();
  }

  onOpenChange(event: boolean) {
    if (!event) {
      this.elem.nativeElement.value = '';
    }
  }

}
