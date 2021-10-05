import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService, UserService} from "@core/services";
import {ChangePasswordCmd} from "@shared/model";
import {Location} from '@angular/common'
import {I18nService} from "@dcc-commons-ng/services";
import {TranslateService} from "@ngx-translate/core";
import {ToastService} from "@shared/components/toast";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;
  loading = false;
  constructor(private router: Router,
              private i18nService: I18nService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private location: Location,
              private authenticationService: AuthenticationService,
              private translate: TranslateService,
              private toastr: ToastService) { }

  ngOnInit() {
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
        this.loading = false;
        this.toastr.success(this.translate.instant('change-password.success_text'), this.translate.instant('change-password.title'));
        this.logout();
      }
    });
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
