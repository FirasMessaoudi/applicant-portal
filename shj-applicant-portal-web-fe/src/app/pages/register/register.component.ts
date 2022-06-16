import {Component, ElementRef, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {environment} from "@env/environment";
import {NgbDateStruct, NgbDropdown, NgbModal, NgbTypeahead} from "@ng-bootstrap/ng-bootstrap";
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
import {merge, Observable, Subject, Subscription} from "rxjs";
import {filter, map} from "rxjs/operators";
import {LookupService} from '@app/_core/utilities/lookup.service';

@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'register.component.html',
  styleUrls: ['register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {

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
  otpDataSubscription: Subscription;
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
  dateStructHijri: any;
  applicantCountry: any;
  registerType = 'uin';
  uin: any;
  countries: CountryLookup[] = [];
  countriesList: CountryLookup[] = [];
  selectedCountryCode = "113";
  selectedNationality ='';
  SAUDI_COUNTRY_CODE = "113";
  selectedCountryName = "SA";
  selectedCountryPrefix: string = "+966";
  @ViewChild('instance')
  instance: NgbTypeahead;
  @ViewChild('elem')
  elem: ElementRef;
  @ViewChild('countryListDropdown')
  countryListDropdown: NgbDropdown;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();

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
    private lookupsService: LookupService,
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
    this.otpDataSubscription = this.authenticationService.otpData.subscribe(data => {
      if (data.user && data.user.uin) {
        this.isApplicantVerified = true;
        let dateStruct = JSON.parse(localStorage.getItem('DATE_STRUCT'));
        this.dateString = this.dateFormatterService.toString(dateStruct);
        this.selectedDateOfBirth = dateStruct;
        if (data.user.dateOfBirthGregorian) {
          this.selectedDateType = DateType.Gregorian;
          this.registerForm.controls['dateOfBirthGregorian'].setValue(this.dateFormatterService.toDate(dateStruct));
          this.dateString = this.dateFormatterService.toString(this.dateFormatterService.toHijri(dateStruct));

          this.registerForm.controls['dateOfBirthHijri'].setValue(this.dateString.split('/').reverse().join(''));

        }
        this.user = data.user;
        this.registerForm.controls['uin'].setValue(this.user.uin);
        this.selectedCountryCode = this.user.countryCode.toLowerCase();
        this.selectedCountryName = this.countriesList.find(country=>country.code==this.selectedCountryCode).countryNamePrefix;
        this.fillRegistrationForm();
      }
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.registerForm.controls;
  }

  register() {

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

    let mobileNumber = this.registerForm.controls['mobileNumber'].value.replace(reg1, "").replace(reg3, "");
    this.registerForm.controls['mobileNumber'].setValue(mobileNumber);

    this.registerService.generateOTPForRegistration(this.registerForm.value, this.selectedCountryPrefix.replace(reg2, ""), this.captchaElem.getCurrentResponse()).subscribe(response => {
      if (!response) {
        console.log(response);
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
          this.user.mobileNumber = this.registerForm.controls.mobileNumber.value.replace(reg1, "").replace(reg3, "");
          this.user.countryCode = this.selectedCountryCode.toUpperCase();
          this.user.countryPhonePrefix = this.selectedCountryPrefix.replace(reg2, "")
          this.user.email = this.registerForm.controls.email.value;
          this.user.password = this.registerForm.controls.password.value;
          this.user.preferredLanguage = this.currentLanguage.startsWith('ar') ? "ar" : "en";
          this.user.nationalityCode = this.selectedNationality;
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

  idFieldName(){
    if(this.registerType == 'uin')
        return this.translate.instant("register.smart_id_number")
    if(this.registerType == 'id')
        return this.translate.instant("register.id_number")
    if(this.registerType == 'passport')
        return this.translate.instant("register.passport_id_number")
  }

  private createForm() {

    this.registerForm = this.formBuilder.group({
      uin: [''],
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

  lookupService(): LookupService {
    return this.lookupsService;
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      result.forEach(c => c.countryPhonePrefix = '+' + c.countryPhonePrefix);
      this.countries = result;
    });
    this.cardService.findCountries().subscribe(result => {
      this.countriesList = result;
    });
  }

  verifyApplicant() {

    this.loading = true;
    this.isApplicantVerified = false;
    let gregorianDate = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.datepipe.transform(this.registerForm?.controls.dateOfBirthGregorian.value, 'yyyy-MM-dd') : null;
    let hijriDate = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? null : this.registerForm?.controls.dateOfBirthHijri.value;
    this.registerService.verifyApplicant(this.registerType, this.registerForm?.controls?.uin.value, gregorianDate, hijriDate, this.selectedNationality).subscribe(response => {
      if (response) {
        this.user = response;
        console.log(response.digitalIds[0].uin);
        this.user.uin = response.digitalIds[0].uin;
        console.log(this.user);
        this.fillRegistrationForm();
        if (this.user.mobileNumber) {
          let applicantMobileNumber;
          this.selectedCountryCode = this.SAUDI_COUNTRY_CODE.toLowerCase();
          if (response.hasLocalMobileNumber) {
            applicantMobileNumber = this.user.mobileNumber.substring(this.user.mobileNumber.length - 9);
            this.registerForm.controls['mobileNumber'].setValue(applicantMobileNumber);
          } else {
            if (this.user.countryCode) {
              this.selectedCountryCode = response.countryCode?.toLowerCase().substr(0, 2);
            }
            let dialCode = this.countries.find(c => this.selectedCountryCode?.toLowerCase() === c.code?.toLowerCase())?.countryPhonePrefix;
            if (this.user.mobileNumber.startsWith('00')) {
              console.log("starts with 00")
              this.user.mobileNumber = this.user.mobileNumber.substring(2);
            }
            applicantMobileNumber = this.user.mobileNumber.replace(dialCode, '');
            this.registerForm.controls['mobileNumber'].setValue(applicantMobileNumber);
          }
          this.originalMobileNo = applicantMobileNumber;

        }
        this.isApplicantVerified = true;
        this.registerForm.markAsUntouched();

      } else {
        this.isApplicantVerified = false;
        this.toastr.warning(this.translate.instant("register.applicant_not_found"), this.translate.instant("register.verification_error"));
      }
      this.loading = false;
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
      this.loading = false
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
      let dateStruct;
      if (this.dateOfBirthPicker.selectedDateType == DateType.Gregorian) {
        this.dateStructGreg = event;
        this.dateStructHijri = this.dateFormatterService.toHijri(event);
        dateStruct = this.dateFormatterService.toHijri(event);

      } else {
        this.dateStructGreg = this.dateFormatterService.toGregorian(event);
        this.dateStructHijri = event;
        dateStruct = this.dateFormatterService.toGregorian(event);

      }

      localStorage.setItem('DATE_STRUCT', JSON.stringify(this.dateStructGreg));
      this.dateString = this.dateFormatterService.toString(dateStruct);
      this.registerForm.controls.dateOfBirthGregorian.setValue(this.dateFormatterService.toDate(this.dateStructGreg));
      this.registerForm.controls.dateOfBirthHijri.setValue(this.dateFormatterService.toString(this.dateStructHijri).split('/').reverse().join(''));
    } else if (event == null) {
      this.dateString = '';
      this.registerForm.controls.dateOfBirthGregorian.setErrors({'required': true})
      this.registerForm.controls.dateOfBirthGregorian.markAsTouched({onlySelf: true});
    }
  }

  getPreferredCountries(): Array<any> {
    const preferredCountries = [CountryISO.SaudiArabia, this.originalCountryCode?.toLowerCase()];
    const uniqueCountrySet = new Set(preferredCountries);
    return [...uniqueCountrySet];
  }


  goBack() {
    this.router.navigate(['/login'])
  }

  fillRegistrationForm() {
    this.registerForm.controls['fullNameEn'].setValue(this.user.fullNameEn);
    this.registerForm.controls['fullNameAr'].setValue(this.user.fullNameAr);
    this.registerForm.controls['email'].setValue(this.user.email);
    this.registerForm.controls['mobileNumber'].setValue(this.user.mobileNumber);
    this.registerForm.controls['uin'].setValue(this.user.uin);
    this.originalEmail = this.user.email;
    this.originalCountryCode = this.selectedCountryCode;
  }

  ngAfterViewInit() {
    document.querySelector('body').classList.add('register');
  }

  selectNationality(val){
    console.log(val);
    this.selectedNationality = val;
  }

  ngOnDestroy() {
    this.otpDataSubscription.unsubscribe();
    document.querySelector('body').classList.remove('register');
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
    this.selectedCountryName = $event.item.countryNamePrefix;
    this.selectedCountryCode = $event.item.code.toLowerCase();
    this.countryListDropdown.close();
  }

  onOpenChange(event: boolean) {
    if (!event) {
      this.elem.nativeElement.value = '';
    }
  }
}
