import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {environment} from "@env/environment";
import {NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {InvisibleReCaptchaComponent} from "ngx-captcha";
import {ToastService} from "@shared/components/toast/toast-service";
import {TranslateService} from "@ngx-translate/core";
import {RegisterService} from "@core/services/register/register.service";
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/datepicker/consts";
import {HijriGregorianDatepickerComponent} from "@shared/modules/hijri-gregorian-datepicker/datepicker/hijri-gregorian-datepicker.component";
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/datepicker/date-formatter.service";
import {CardService, DEFAULT_MAX_USER_AGE} from "@core/services";
import {DccValidators} from "@shared/validators";
import {DatePipe} from "@angular/common";
import {User} from "@shared/model";
import {CountryISO} from 'ngx-intl-tel-input';
import {CountryLookup} from "@model/country-lookup.model";

@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'register.component.html',
  styleUrls: ['register.component.scss']
})
export class RegisterComponent implements OnInit {

  model: NgbDateStruct;
  isValid: boolean = true;
  error: string;
  registerForm: FormGroup;
  loading = false;
  recaptchaSiteKey: any;
  _minPickerDate: any;
  _maxPickerDate: any;

  isApplicantVerified: boolean = false;
  fullName: string;
  user: User;
  showSuccessPage: boolean = false;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;

  @ViewChild('reCaptchaEl')
  captchaElem: InvisibleReCaptchaComponent;

  @ViewChild('termsCheckbox')
  termsCheckbox: ElementRef;

  recaptcha: any = null;

  selectedDateOfBirth: NgbDateStruct;
  maxDateOfBirthGregorian: NgbDateStruct;
  maxDateOfBirthHijri: NgbDateStruct;
  minDateOfBirthHijri: NgbDateStruct;
  minDateOfBirthGregorian: NgbDateStruct;
  dateString: string;
  selectedDateType: any;
  dateStructGreg: any;
  applicantCountry: any;
  countries: CountryLookup[] = [];
  selectedCountryCode = "SA";

  SAUDI_COUNTRY_CODE = "SA";

  @ViewChild('datePicker') dateOfBirthPicker: HijriGregorianDatepickerComponent;

  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private registerService: RegisterService,
    private cardService: CardService,
    private toastr: ToastService,
    private translate: TranslateService,
    private dateFormatterService: DateFormatterService,
    public datepipe: DatePipe
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.loadLookups();
    // calendar default;
    let toDayGregorian = this.dateFormatterService.todayGregorian();
    let toDayHijri = this.dateFormatterService.todayHijri();
    this.maxDateOfBirthGregorian = {
      year: toDayGregorian.year - DEFAULT_MAX_USER_AGE,
      month: toDayGregorian.month,
      day: toDayGregorian.day
    };
    this.minDateOfBirthGregorian = {
      year: toDayGregorian.year - DEFAULT_MAX_USER_AGE - 100,
      month: toDayGregorian.month + 1,
      day: toDayGregorian.day
    };
    this.maxDateOfBirthHijri = {
      year: toDayHijri.year - DEFAULT_MAX_USER_AGE,
      month: toDayHijri.month,
      day: toDayHijri.day
    };
    this.minDateOfBirthHijri = {
      year: toDayHijri.year - DEFAULT_MAX_USER_AGE - 100,
      month: toDayHijri.month + 1,
      day: toDayHijri.day
    };

    this.selectedDateType = DateType.Gregorian;
    this.showSuccessPage = false;
    this.createForm();

    this.recaptchaSiteKey = environment.invisibleRecaptchaSiteKey;

    this._minPickerDate = {
      year: new Date().getFullYear() - 100,
      month: new Date().getMonth() + 1,
      day: new Date().getDate()
    };
    this._maxPickerDate = {
      year: new Date().getFullYear() - 18,
      month: new Date().getMonth() + 1,
      day: new Date().getDate()
    };

  }

  // convenience getter for easy access to form fields
  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {

    // trigger all validations
    Object.keys(this.registerForm.controls).forEach(field => {
      const control = this.registerForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.registerForm.invalid) {
      return;
    }

    let reg1 = / /g;
    let reg2 = /\+/gi;
    let reg3 = /\-/gi;

    let mobileNumber = this.registerForm.controls['mobileNumber'].value.number.replace(reg1, "").replace(reg3, "");
    this.registerForm.controls['mobileNumber'].setValue(mobileNumber);

    this.registerService.generateOTPForRegistration(this.registerForm.value, this.captchaElem.getCurrentResponse()).subscribe(response => {
      if (!response) {
        this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("register.header_title"));
        this.captchaElem.reloadCaptcha();

      } else {
        if (response.hasOwnProperty("errors") && response.errors) {
          Object.keys(this.registerForm.controls).forEach(field => {
            console.log("looking for validation errors for : " + field);
            if (response.errors[field]) {
              const control = this.registerForm.get(field);
              control.setErrors({invalid: response.errors[field].replace(/\{/, '').replace(/\}/, '')});
              control.markAsTouched({onlySelf: true});
            }
          });
        } else {

          let updateAdminRequired = this.originalMobileNo != this.registerForm.controls['mobileNumber'].value ||
            this.originalEmail != this.registerForm.controls['email'].value || this.originalCountryCode != this.selectedCountryCode.toUpperCase();

          this.user.otpExpiryMinutes = response.otpExpiryMinutes;
          this.user.maskedMobileNumber = response.mobileNumber;
          this.user.uin = this.registerForm.controls.uin.value;
          this.user.mobileNumber = this.registerForm.controls.mobileNumber.value.number.replace(reg1, "").replace(reg3, "");
          this.user.countryCode = this.registerForm.controls.mobileNumber.value.countryCode;
          this.user.countryPhonePrefix = this.registerForm.controls.mobileNumber.value.dialCode.replace(reg2, "")
          this.user.email = this.registerForm.controls.email.value;
          this.user.password = this.registerForm.controls.password.value;
          this.user.preferredLanguage = this.currentLanguage.startsWith('ar') ? "ar" : "en";
          this.authenticationService.updateOtpSubject({
            user: this.user,
            actionType: "/register",
            updateAdmin: updateAdminRequired
          });
          this.router.navigate(['/otp'], {replaceUrl: true});
        }
      }
    });


  }

  private createForm() {

    this.registerForm = this.formBuilder.group({
      uin: ['', [Validators.required]],
      fullNameEn: {disabled: true},
      fullNameAr: {disabled: true},
      dateOfBirthGregorian: ['', Validators.required],
      dateOfBirthHijri: ['', Validators.required],
      mobileNumber: ['', Validators.required],
      email: ['', [DccValidators.email, Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      recaptcha: [''],
    }, {validator: this.passwordMatchValidator});
  }

  passwordMatchValidator(frm: FormGroup) {
    return frm.controls['password'].value === frm.controls['confirmPassword'].value ? null : {'mismatch': true};
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      this.countries = result;
    });
  }

  verifyApplicant() {
    this.isApplicantVerified = false;
    this.registerService.verifyApplicant(this.registerForm?.controls?.uin.value, this.datepipe.transform(this.registerForm?.controls.dateOfBirthGregorian.value, 'yyyy-MM-dd'), this.registerForm?.controls.dateOfBirthHijri.value).subscribe(response => {
      if (response && !this.checkNullProperties(response)) {
        this.user = response;
        this.registerForm.controls['fullNameEn'].setValue(this.user.fullNameEn);
        this.registerForm.controls['fullNameAr'].setValue(this.user.fullNameAr);
        this.registerForm.controls['email'].setValue(this.user.email);
        this.registerForm.controls['mobileNumber'].setValue(this.user.mobileNumber);

        let applicantMobileNumber;
        if (response.hasLocalMobileNumber) {
          this.selectedCountryCode = this.SAUDI_COUNTRY_CODE.toLowerCase();
          applicantMobileNumber = this.user.mobileNumber.substring(this.user.mobileNumber.length - 9);
          this.registerForm.controls['mobileNumber'].setValue(applicantMobileNumber);
        } else {
          this.selectedCountryCode = response.countryCode?.toLowerCase().substr(0, 2);
          let dialCode = this.countries.find(c => this.selectedCountryCode.toLowerCase() === c.code.toLowerCase())?.countryPhonePrefix;
          applicantMobileNumber = this.user.mobileNumber.replace(dialCode, '');
          this.registerForm.controls['mobileNumber'].setValue(applicantMobileNumber);
        }

        this.isApplicantVerified = true;
        this.originalMobileNo = applicantMobileNumber;
        this.originalEmail = this.user.email;
        this.originalCountryCode = this.selectedCountryCode;
        this.registerForm.markAsUntouched();

      } else {
        this.isApplicantVerified = false;
        this.toastr.warning(this.translate.instant("register.applicant_not_found"), this.translate.instant("register.verification_error"));
      }
    }, error => {
      console.log(error);
      this.registerForm.markAsUntouched();
      this.isApplicantVerified = false;
      // this.showCaptcha = (error.status == 555);
      this.error = error;
      if (error.status == 560) {
        this.toastr.warning(this.translate.instant("register.user_already_registered"), this.translate.instant("register.verification_error"));
      } else if (error.status == 561) {
        this.toastr.warning(this.translate.instant("register.applicant_not_found"), this.translate.instant("register.verification_error"));
      } else {
        this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("register.header_title"));
      }
    });
  }

  onCaptchaLoad() {
    console.log('captcha is loaded');
  }

  onCaptchaReady() {
    console.log('captcha is ready');
    this.captchaElem.execute();
  }

  onCaptchaSuccess(captchaResponse: string): void {
    console.log(captchaResponse);
  }

  onDateOfBirthChange(event) {
    if (event) {
      let dateStruct = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : this.dateFormatterService.toGregorian(event);
      this.dateStructGreg = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? event : this.dateFormatterService.toGregorian(event);
      let dateStructHijri = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : event;
      this.dateString = this.dateFormatterService.toString(dateStruct);
      this.registerForm.controls.dateOfBirthGregorian.setValue(this.dateFormatterService.toDate(this.dateStructGreg));
      this.registerForm.controls.dateOfBirthHijri.setValue(this.dateFormatterService.toString(dateStructHijri).split('/').reverse().join(''));
    } else if (event == null) {
      this.dateString = '';
      this.registerForm.controls.dateOfBirthGregorian.setErrors({'required': true})
      this.registerForm.controls.dateOfBirthGregorian.markAsTouched({onlySelf: true});
    }
  }

  checkNullProperties(obj: any) {
    for (let key in obj) {
      if (obj[key] !== null)
        return false;
    }
    return true;
  }

  getPreferredCountries(): Array<any> {
    const preferredCountries = [CountryISO.SaudiArabia, this.originalCountryCode];
    const uniqueCountrySet = new Set(preferredCountries);
    return [...uniqueCountrySet];
  }

  goBack() {
    this.router.navigate(['/login'])
  }
}
