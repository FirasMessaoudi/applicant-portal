import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";
import {ReCaptcha2Component, ReCaptchaV3Service} from "ngx-captcha";
import {environment} from "@env/environment";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Title} from "@angular/platform-browser";
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";
import {UserService} from "@core/services";


@Component({
  encapsulation: ViewEncapsulation.None,
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.scss']
})
export class LoginComponent implements OnInit {

  returnUrl: string;
  error: any;
  loginForm: FormGroup;
  loading = false;
  showCaptcha = false;
  recaptchaSiteKey: any;

  @ViewChild('reCaptchaEl')
  captchaElem: ReCaptcha2Component;
  supportedLanguages: Lookup[];
  localizedSupportedLanguages: Lookup[];
  selectedLang: Lookup;

  constructor(private modalService: NgbModal,
              private formBuilder: FormBuilder,
              private i18nService: I18nService,
              private route: ActivatedRoute,
              private router: Router,
              private reCaptchaV3Service: ReCaptchaV3Service,
              private authenticationService: AuthenticationService,
              private titleService: Title,
              private translate: TranslateService,
              private lookupService: LookupService,
              private userService: UserService) {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
    this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.translate.get('general.app_title').subscribe((res: string) => {
        this.titleService.setTitle(res);
      });
    });
  }

  ngOnInit() {
    this.loadLookups();
    this.recaptchaSiteKey = environment.recaptchaSiteKey;
    this.authenticationService.updateSubject(null);
    this.modalService.dismissAll();
    this.createForm();
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.titleService.setTitle(this.translate.instant('general.app_title'));
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    // trigger all validations
    Object.keys(this.loginForm.controls).forEach(field => {
      const control = this.loginForm.get(field);
      control.markAsTouched({onlySelf: true});
    });
    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.loginForm.value.username, this.loginForm.value.password, null)
      .pipe(finalize(() => {
        this.loginForm.markAsPristine();
        this.loading = false;
      }))
      .subscribe(user => {
        console.log(user);
        //TODO:to be checked in which scenario it is called or we have to remove this check for password expiry

        this.userService.updatePreferredLang(this.selectedLang.code, this.loginForm.value.username).subscribe(response => {
        });
        if (user.passwordExpired) {
          console.log('redirect to change password page');
          this.router.navigate(['/change-password'], {replaceUrl: true});
        } else if (user.otpRequired) {
          console.log('redirect to otp page');
          user.maskedMobileNumber = user.mobileNumber;
          this.authenticationService.updateOtpSubject({user: user, actionType: "/login"});

          this.router.navigate(['/otp'], {replaceUrl: true});

        } else {
          console.log('redirect to / page');
          this.router.navigate(['/'], {replaceUrl: true});
        }
      }, error => {
        console.log(error);
        this.showCaptcha = (error.status == 555);
        this.error = error;
      });
  }

  private createForm() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.required],
      recaptcha: ['']
    });
  }

  onCaptchaLoad() {
    console.log('captcha is loaded');
  }

  onCaptchaReady() {
    console.log('captcha is ready');
  }

  refreshRegisterData() {
    this.authenticationService.updateOtpSubject({});
  }


  onCaptchaSuccess(captchaResponse: string): void {
    console.log(captchaResponse);
  }

  loadLookups() {
    this.authenticationService.findSupportedLanguages().subscribe(result => {
      this.supportedLanguages = result;
      this.localizedSupportedLanguages = this.supportedLanguages.filter(item => item.lang.toLowerCase() === item.code.toLowerCase());
    //TODO:remove this second filtration when we have other supported languages
      //this.localizedSupportedLanguages = this.localizedSupportedLanguages.filter(item => (item.lang.toLowerCase() === "ar" || item.lang.toLowerCase() === "en"));
      this.selectedLang = new Lookup();
      this.selectedLang = this.localizedSupportedLanguages.find(item => item.lang.toLowerCase() === (this.currentLanguage.startsWith('ar') ? "ar" : "en"));
      this.setLanguage(this.selectedLang.lang.toLowerCase());
    });

  }

  onLangSelect(lang) {

    this.selectedLang = lang;
    this.setLanguage(lang.lang.toLowerCase());
  }


}
