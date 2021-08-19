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
import {DatePipe, Location} from "@angular/common";
import {User} from "@shared/model";

import {Observable} from 'rxjs';
import {debounceTime, map} from 'rxjs/operators';
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
  SAUDI_MOBILE_NUMBER_REGEX: RegExp = new RegExp("^(009665|9665|\\+9665|05|5)([0-9]{8})$");
  isSaudiNumber: boolean;
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
  selectedCountryCode = "ae";
  formattedCountryDial: any;

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
    private location: Location,
    public datepipe: DatePipe
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  search: (text$: Observable<string>) => Observable<CountryLookup[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      map(term => term === '' ? this.countries
        // : this.statesWithFlags.filter(v => v.name.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
        : this.countries.filter(v => ("+" + v.countryPhonePrefix).toLowerCase().indexOf(term.toLowerCase()) > -1))
    )

  formatter = (x: { countryPhonePrefix: string }) => x.countryPhonePrefix;

  public onFocus(e: Event): void {
    e.stopPropagation();
    setTimeout(() => {
      const inputEvent: Event = new Event('input');
      e.target.dispatchEvent(inputEvent);
    }, 0);
  }

  selectedItem($event) {
    this.registerForm.controls["countryPhonePrefix"].setValue($event.item.countryPhonePrefix);
    this.selectedCountryCode = $event.item.code.toLowerCase();
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

          let reg1 = /\+/gi;
          // Use of String replace() Method
          let currentPhonePrefix = this.registerForm.controls['countryPhonePrefix'].value;
          this.formattedCountryDial = currentPhonePrefix.countryPhonePrefix?.replace(reg1, "00");
          this.user.mobileNumber = this.registerForm.controls.mobileNumber.value;
          this.user.countryCode = this.selectedCountryCode.toUpperCase();
          this.user.countryPhonePrefix = this.formattedCountryDial;
          this.user.email = this.registerForm.controls.email.value;
          this.user.password = this.registerForm.controls.password.value;
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
      mobileNumber: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
      email: ['', [DccValidators.email, Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      recaptcha: [''],
      countryPhonePrefix: [{countryPhonePrefix: ''}, [Validators.required]]
    }, {validator: this.passwordMatchValidator});


  }

  passwordMatchValidator(frm: FormGroup) {
    return frm.controls['password'].value === frm.controls['confirmPassword'].value ? null : {'mismatch': true};
  }


  verifyApplicant() {
    this.isApplicantVerified = false;
    this.registerService.verifyApplicant(this.registerForm?.controls?.uin.value, this.datepipe.transform(this.registerForm?.controls.dateOfBirthGregorian.value, 'yyyy-MM-dd'), this.registerForm?.controls.dateOfBirthHijri.value).subscribe(response => {
      if (response) {
        this.user = response;
        this.registerForm.controls['fullNameEn'].setValue(this.user.fullNameEn);
        this.registerForm.controls['fullNameAr'].setValue(this.user.fullNameAr);
        this.registerForm.controls['email'].setValue(this.user.email);


        this.isSaudiNumber = this.SAUDI_MOBILE_NUMBER_REGEX.test(this.user.mobileNumber);

        if (this.isSaudiNumber) {
          this.applicantCountry = this.countries.filter(c => "SA" === c.code && 'EN' === c.lang.toUpperCase());
        } else {
          this.applicantCountry = this.countries.filter(c => response.countryCode.toUpperCase() === c.code && 'EN' === c.lang.toUpperCase());
        }
        this.selectedCountryCode = this.applicantCountry[0].code.toLowerCase();
        this.registerForm.controls["countryPhonePrefix"].setValue({countryPhonePrefix: this.applicantCountry[0].countryPhonePrefix});
        let reg1 = /\+/gi;
        let reg2 = /\-/gi;
        // Use of String replace() Method
        this.formattedCountryDial = this.applicantCountry[0].countryPhonePrefix.replace(reg1, "00").replace(reg2, "");
        let applicantMobileNumber = this.user.mobileNumber.length > 10 ? this.user.mobileNumber.substring(this.formattedCountryDial.length)
          : (this.user.mobileNumber.length == 10 ? this.user.mobileNumber.substring(1) : this.user.mobileNumber);
        this.registerForm.controls['mobileNumber'].setValue(applicantMobileNumber);
        this.isApplicantVerified = true;
        this.originalMobileNo = applicantMobileNumber
        this.originalEmail = this.user.email;
        this.originalCountryCode = this.selectedCountryCode.toUpperCase();
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

  loadLookups() {
    this.cardService.findCountries().subscribe(result => this.countries = this.removeDuplicates(result, 'code'));
  }

  removeDuplicates(originalArray, prop) {
    let i;
    const newArray = [];
    const lookupObject = {};

    for (i in originalArray) {
      lookupObject[originalArray[i][prop]] = originalArray[i];
    }

    for (i in lookupObject) {
      newArray.push(lookupObject[i]);
    }
    return newArray;
  }


  goBack() {
    this.router.navigate(['/login'])
  }
}
