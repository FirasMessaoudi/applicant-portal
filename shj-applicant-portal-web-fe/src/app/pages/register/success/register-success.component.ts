import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, PatternValidator, Validators} from '@angular/forms';
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
import {User} from "@shared/model";

@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'register-success.component.html',
  styleUrls: ['register-success.component.scss']
})
export class RegisterSuccessComponent {

  recaptcha: any = null;

  dateString: string;

  constructor(
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private router: Router,
    private authenticationService: AuthenticationService,

  ) {
    alert("heeeeeeeeeeeeeeeeer")
    // redirect to home if already logged in
    // if (this.authenticationService.isAuthenticated()) {
    //   this.router.navigate(['/']);
    // }
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  goBack() {
    this.router.navigate(['/login'])
  }
}
