import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {environment} from "@env/environment";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {InvisibleReCaptchaComponent} from "ngx-captcha";
import {ToastService} from "@shared/components/toast/toast-service";
import {TranslateService} from "@ngx-translate/core";
import {RegisterService} from "@core/services/register/register.service";
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/datepicker/consts";
import {HijriGregorianDatepickerComponent} from "@shared/modules/hijri-gregorian-datepicker/datepicker/hijri-gregorian-datepicker.component";
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/datepicker/date-formatter.service";
import {DEFAULT_MAX_USER_AGE} from "@core/services";
import {DccValidators, IdType} from "@shared/validators";
import {Location} from "@angular/common";

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

  verify: boolean=false;

  @ViewChild('reCaptchaEl')
  captchaElem: InvisibleReCaptchaComponent;

  @ViewChild('termsCheckbox')
  termsCheckbox: ElementRef;

  recaptcha: any = null;

  selectedDateOfBirth: NgbDateStruct;
  maxDateOfBirthGregorian: NgbDateStruct;
  maxDateOfBirthHijri: NgbDateStruct;
  dateString: string;
  selectedDateType: any;

  @ViewChild('datePicker') dateOfBirthPicker: HijriGregorianDatepickerComponent;

  constructor(
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private registerService: RegisterService,
    private toastr: ToastService,
    private translate: TranslateService,
    private dateFormatterService: DateFormatterService,
    private location: Location,
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
    // calendar default;
    let toDayGregorian = this.dateFormatterService.todayGregorian();
    let toDayHijri = this.dateFormatterService.todayHijri();
    this.maxDateOfBirthGregorian = {
      year: toDayGregorian.year - DEFAULT_MAX_USER_AGE,
      month: toDayGregorian.month,
      day: toDayGregorian.day
    };
    this.maxDateOfBirthHijri = {
      year: toDayHijri.year - DEFAULT_MAX_USER_AGE,
      month: toDayHijri.month,
      day: toDayHijri.day
    };
    this.selectedDateType = DateType.Gregorian;

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

    // check terms and conditions
    if (!this.termsCheckbox.nativeElement.checked) {
      this.toastr.warning(this.translate.instant("register.link_legal_accept"), this.translate.instant("general.dialog_alert_title"));
      return;
    }

    // stop here if form is invalid
    if (this.registerForm.invalid) {
      return;
    }

    // everything is fine we move to recaptcha check
    // if we get a successful response from recaptcha, then we send the form
    this.registerService.register(this.registerForm.value, this.captchaElem.getCurrentResponse()).subscribe(response => {
      if (response) {
        this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("register.title"));
        this.captchaElem.reloadCaptcha();
        if (response.hasOwnProperty("errors") && response.errors) {
          Object.keys(this.registerForm.controls).forEach(field => {
            console.log("looking for validation errors for : " + field);
            if (response.errors[field]) {
              const control = this.registerForm.get(field);
              control.setErrors({invalid: response.errors[field].replace(/\{/, '').replace(/\}/, '')});
              control.markAsTouched({onlySelf: true});
            }
          });
        }
      } else {
        this.toastr.success(this.translate.instant("general.dialog_add_success_text"), this.translate.instant("register.title"));
        this.router.navigate(['/login.html']);
      }
    });
  }

  private createForm() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', [DccValidators.charactersOnly(), Validators.required]],
      familyName: ['', [DccValidators.charactersOnly(), Validators.required]],
      gender: ['M', Validators.required],
      nin: ['', Validators.compose([Validators.required, DccValidators.ninOrIqama(IdType.NIN_OR_IQAMA)])],
      dateOfBirthGregorian: ['', Validators.required],
      dateOfBirthHijri: ['', Validators.required],
      mobileNumber: ['', Validators.compose([Validators.required, DccValidators.mobileNumber])],
      email: ['', Validators.required],
      recaptcha: ['']
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
      let dateStructGreg = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? event : this.dateFormatterService.toGregorian(event);
      let dateStructHijri = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : event;
      this.dateString = this.dateFormatterService.toString(dateStruct);
      this.registerForm.controls.dateOfBirthGregorian.setValue(this.dateFormatterService.toDate(dateStructGreg));
      this.registerForm.controls.dateOfBirthHijri.setValue(this.dateFormatterService.toString(dateStructHijri).split('/').reverse().join(''));
    }
  }


  goBack() {
    this.location.back();
  }
}
