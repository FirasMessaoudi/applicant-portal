import {AfterViewInit, Component, OnInit, ViewChildren, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {finalize} from "rxjs/operators";
import {interval, Subscription} from "rxjs";

@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'otp.component.html',
  styleUrls: ['otp.component.scss']
})
export class OtpComponent implements OnInit, AfterViewInit {

  otpData: any;
  mask: string;
  error: string;
  otpForm: FormGroup;
  loading = false;
  timerContent: string = '';
  timerSubscription: Subscription;
  formInputs = ['input1', 'input2', 'input3', 'input4'];
  @ViewChildren('formRow') rows: any;

  constructor(
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.isAuthenticated()) {
      // FIXME the timer is expiring before the token expiry cookie
      // removing it for now to fix redirecting issue
      // this.router.navigate(['/']);
    }
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.createForm();
    this.authenticationService.otpData.subscribe(data => {
      if (!data || !data.otpExpiryMinutes) {
        this.goBack();
      }
      this.otpData = data;
      this.startTimer(data.otpExpiryMinutes);
      this.mask = data.mobileNumber;
    });
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
    this.authenticationService.validateOtp(this.otpData.name, pin)
      .pipe(finalize(() => {
        this.otpForm.markAsPristine();
        this.loading = false;
      })).subscribe(user => {
      console.log(user);
      if (this.timerSubscription) {
        this.timerSubscription.unsubscribe();
      }
      // login successful if there's a jwt token in the response
      this.authenticationService.updateSubject(user);
      if (user.passwordExpired) {
        console.log('redirect to change password page');
        // redirect to change password page
        this.router.navigate(['/change-password'], {replaceUrl: true});
      } else {
        console.log('redirect to / page');
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
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
    this.router.navigate(['/login']);
  }

  startTimer(durationMinutes) {
    let timer = durationMinutes * 60;
    let minutes;
    let seconds;

    this.timerSubscription = interval(1000).subscribe(x => {
      minutes = Math.floor(timer / 60);
      seconds = Math.floor(timer % 60);

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;

      this.timerContent = minutes + ":" + seconds;

      --timer;
      if (--timer < 0) {
        this.goBack();
      }
    })
  }
}
