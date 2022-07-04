import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService, UserService} from "@core/services";
import {ChangePasswordCmd} from "@shared/model";
import {Location} from '@angular/common'
import {TranslateService} from "@ngx-translate/core";
import {ToastService} from "@shared/components/toast";
import {Lookup} from "@model/lookup.model";
import { CustomI18nService } from '@app/_core/utilities/custom-i18n.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;
  loading = false;
  localizedSupportedLanguages: Lookup[];
  selectedLang: Lookup;
  currentUser: any;
  supportedLanguages: Lookup[];

  constructor(private router: Router,
              private i18nService: CustomI18nService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private location: Location,
              private authenticationService: AuthenticationService,
              private translate: TranslateService,
              private toastr: ToastService) {
  }

  ngOnInit() {
    this.loadLookups();
    this.createForm();
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  private createForm() {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      newPasswordConfirm: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]]
    }, {validator: this.passwordMatchValidator});
  }


  loadLookups() {
    this.authenticationService.findSupportedLanguages().subscribe(result => {
      this.supportedLanguages = result;
      this.localizedSupportedLanguages = this.supportedLanguages.filter(item => item.lang.toLowerCase() === item.code.toLowerCase());
      //TODO:remove this second filtration when we have other supported languages
      // this.localizedSupportedLanguages = this.localizedSupportedLanguages.filter(item => (item.lang.toLowerCase() === "ar" || item.lang.toLowerCase() === "en"));
      this.selectedLang = new Lookup();
      this.selectedLang = this.localizedSupportedLanguages.find(item => item.lang.toLowerCase() === (this.currentLanguage.slice(0,2)));
      this.setLanguage(this.selectedLang.lang.toLowerCase());
    });

  }

  // convenience getter for easy access to form fields
  get f() {
    return this.changePasswordForm.controls;
  }

  passwordMatchValidator(frm: FormGroup) {
    return frm.controls['newPassword'].value === frm.controls['newPasswordConfirm'].value ? null : {'mismatch': true};
  }

  changePassword() {
    this.loading = true;
    let changePasswordCmd:ChangePasswordCmd = new ChangePasswordCmd();
    changePasswordCmd.oldPassword = this.changePasswordForm.controls['oldPassword'].value;
    changePasswordCmd.newPassword = this.changePasswordForm.controls['newPassword'].value;
    changePasswordCmd.newPasswordConfirm = this.changePasswordForm.controls['newPasswordConfirm'].value;
    this.userService.changePassword(changePasswordCmd).subscribe(response => {
      if (response && (response.hasOwnProperty("errors") && response.errors)) {
        Object.keys(this.changePasswordForm.controls).forEach(field => {
          console.log("looking for validation errors for : " + field);
          if (response.errors[field]) {
            const control = this.changePasswordForm.get(field);
            control.setErrors({invalid: response.errors[field].replace(/\{/, '').replace(/\}/, '')});
            control.markAsTouched({onlySelf: true});
          }
        });

        if (response.errors['general error']) {
          this.toastr.warning(this.translate.instant('general.dialog_error_text'), this.translate.instant('change-password.title'));

        }

      } else {
        this.toastr.success(this.translate.instant('change-password.success_text'), this.translate.instant('change-password.title'));
        this.currentUser = this.authenticationService.currentUser;
        this.userService.updatePreferredLang(this.selectedLang.code, this.currentUser?.name).subscribe(response => {
          this.logout();
        });

      }
      this.loading = false;

    });
  }

  onLangSelect(lang) {

    this.selectedLang = lang;
    this.setLanguage(lang.lang.toLowerCase());
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
  goBack() {
    if(this.authenticationService.userHasExpiredPassword()){
      this.logout();
    }else {
      this.location.back();
    }
  }
}
