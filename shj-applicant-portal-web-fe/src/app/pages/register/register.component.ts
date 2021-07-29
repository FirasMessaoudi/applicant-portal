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
import {DEFAULT_MAX_USER_AGE} from "@core/services";
import {DccValidators} from "@shared/validators";
import {DatePipe, Location} from "@angular/common";
import {User} from "@shared/model";

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
  dateStructGreg: any;

  @ViewChild('datePicker') dateOfBirthPicker: HijriGregorianDatepickerComponent;


  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private registerService: RegisterService,
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
    this.showSuccessPage=false;
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


    this.user.email = this.registerForm.controls['email'].value;
    this.user.mobileNumber = this.registerForm.controls['localMobileNumber'].value;
    this.user.password = this.registerForm.controls['password'].value;
    this.registerService.generateOTPForRegistration(this.user, this.captchaElem.getCurrentResponse()).subscribe(response => {
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
          this.authenticationService.updateOtpSubject({user: response});
          this.router.navigate(['/otp']);
          this.authenticationService.getOtpVerifiedForRegisterObs().subscribe(response => {
            if (response) {
              let updateAdminRequired = this.originalMobileNo != this.user.mobileNumber ||
                this.originalEmail != this.user.email;
              this.registerService.register(this.user, updateAdminRequired).subscribe(response => {
                if (!response) {
                  this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("register.header_title"));
                  this.captchaElem.reloadCaptcha();
                } else {
                  this.router.navigate(['/register-success'], {replaceUrl: true});
                }
              });

            }
          });
        }

      }

    });


  }

  private createForm() {

    this.registerForm = this.formBuilder.group({
      uin: ['', [Validators.required]],
      fullNameEn: [''],
      fullNameAr: [''],
      dateOfBirthGregorian: ['', Validators.required],
      dateOfBirthHijri: ['', Validators.required],
      localMobileNumber: ['', [DccValidators.mobileNumber, Validators.required]],
      email: ['', [DccValidators.email, Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      recaptcha: ['']
    }, {validator: this.passwordMatchValidator});


  }

  passwordMatchValidator(frm: FormGroup) {
    return frm.controls['password'].value === frm.controls['confirmPassword'].value ? null : {'mismatch': true};
  }

  verifyApplicant() {
    this.isApplicantVerified = false;
    this.registerService.verifyApplicant(this.registerForm.controls.uin.value, this.datepipe.transform(this.registerForm.controls.dateOfBirthGregorian.value, 'yyyy-MM-dd'), this.registerForm.controls.dateOfBirthHijri.value).subscribe(response => {
      if (response) {
        this.user = response;
        this.registerForm.controls['fullNameEn'].setValue(this.user.fullNameEn);
        this.registerForm.controls['fullNameAr'].setValue(this.user.fullNameAr);
        this.registerForm.controls['localMobileNumber'].setValue(this.user.mobileNumber);
        this.registerForm.controls['email'].setValue(this.user.email);
        this.isApplicantVerified = true;
        this.originalMobileNo=this.user.mobileNumber
        this.originalEmail=this.user.email;
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
        this.toastr.warning(this.translate.instant("user already registered"), this.translate.instant("register.verification_error"));
      } else {
        this.toastr.warning(this.translate.instant("user not found"), this.translate.instant("register.verification_error"));
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
    }
  }


  goBack() {

    this.router.navigate(['/login'])
  }
}
