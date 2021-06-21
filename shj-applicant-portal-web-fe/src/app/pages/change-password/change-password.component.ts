import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService, UserService} from "@core/services";
import {ChangePasswordCmd} from "@shared/model";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;

  constructor(private router: Router,
              private i18nService: I18nService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private authenticationService: AuthenticationService) { }

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
      } else {
        this.logout();
      }
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
