import {AfterViewInit, Component, OnDestroy, OnInit, ViewChildren, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {finalize} from "rxjs/operators";
import {Subscription} from "rxjs";
import {Location} from "@angular/common";
import {TranslateService} from "@ngx-translate/core";

@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'otp.component.html',
  styleUrls: ['otp.component.scss']
})
export class OtpComponent implements OnInit, AfterViewInit, OnDestroy {

  otpData: any;
  otpDataSubscription: Subscription;
  mask: string;
  error: string;
  otpForm: FormGroup;
  loading = false;
  timerContent: string = '';
  timerInterval: any;
  formInputs = ['input1', 'input2', 'input3', 'input4'];
  otpTitle: string;
  previousUrl: string;
  @ViewChildren('formRow') rows: any;

  constructor(
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private location: Location,
    private translate: TranslateService) {}

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.createForm();
    this.otpDataSubscription = this.authenticationService.otpData.subscribe(data => {
      if (!data.user || !data.user.otpExpiryMinutes) {
        this.goBack();
      }
      this.previousUrl = data.actionType;
      this.otpTitle = this.previousUrl == "/login" ? this.translate.instant("login.header_title") : this.translate.instant("register.header_title");
      this.otpData = data.user;
      this.startTimer(data.user?.otpExpiryMinutes);
      this.mask = data.user.mobileNumber;
    });
  }

  ngOnDestroy() {
    this.otpDataSubscription.unsubscribe();
  }

  ngAfterViewInit() {
    this.rows._results[0].nativeElement.focus();
  }

  onSubmit() {
    let pin: string = '';
    Object.keys(this.otpForm.controls).forEach(field => {
      const control = this.otpForm.get(field);
      pin += control.value;
    });
    console.log(pin);

    this.loading = true;
    if (this.previousUrl == "/login") {
    this.authenticationService.validateOtpForLogin(this.otpData.name, pin)
      .pipe(finalize(() => {
        this.otpForm.markAsPristine();
        this.loading = false;
      })).subscribe(user => {
      console.log(user);
      clearInterval(this.timerInterval);
      // login successful if there's a jwt token in the response
      this.authenticationService.updateSubject(user);

      if (user.passwordExpired) {
        console.log('redirect to change password page');
        this.router.navigate(['/change-password'], {replaceUrl: true});
      } else {
        console.log('redirect to / page');
        clearInterval(this.timerInterval);
        this.router.navigate(['/'], {replaceUrl: true});
      }
    }, error => {
      console.log(error);
      this.error = error;
      // reset form
      Object.keys(this.otpForm.controls).forEach(field => {
        this.otpForm.get(field).setValue(null);
        this.rows._results[0].nativeElement.focus();
      });
    });
    } else {
      this.authenticationService.validateOtpForRegister(this.otpData.name, pin)
        .pipe(finalize(() => {
          this.otpForm.markAsPristine();
          this.loading = false;
        })).subscribe(user => {
        console.log(user);
        clearInterval(this.timerInterval);
        this.authenticationService.setOtpVerifiedForRegisterObs(user);
      }, error => {
        console.log(error);
        this.error = error;
        // reset form
        Object.keys(this.otpForm.controls).forEach(field => {
          this.otpForm.get(field).setValue(null);
          this.rows._results[0].nativeElement.focus();
        });
      });
    }
  }

  private createForm() {
    const group: any = {};
    this.formInputs.forEach(key => {
      group[key] = new FormControl('', Validators.required);
    });
    this.otpForm = new FormGroup(group);
  }

  keyUpEvent(event, index) {
    let pos = index;
    if (event.keyCode === 8 && event.which === 8) {
      pos = index - 1;
    } else {
      pos = index + 1;
    }
    if (pos > -1 && pos < this.formInputs.length) {
      this.rows._results[pos].nativeElement.focus();
    }
    if (this.otpForm.valid) {
      this.onSubmit();
    }
  }

  goBack() {
    clearInterval(this.timerInterval);
    this.router.navigate([this.previousUrl]);
  }

  startTimer(durationMinutes) {
    let timer = durationMinutes * 60;
    let minutes;
    let seconds;

    this.timerInterval = setInterval(() => {
      minutes = Math.floor(timer / 60);
      seconds = Math.floor(timer % 60);

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;

      this.timerContent = minutes + ":" + seconds;

      --timer;
      if (--timer < 0) {
        this.goBack();
      }
    }, 1000);
  }
}
