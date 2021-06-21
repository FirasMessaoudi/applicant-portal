import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {combineLatest} from "rxjs";
import {map} from "rxjs/operators";
import {ToastService} from '@shared/components/toast/toast-service';
import {I18nService} from "@dcc-commons-ng/services";
import {TranslateService} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {RoleService} from "@core/services/role/role.service";
import {Role} from "@model/role.model";
import {Authority} from "@model/authority.model";
import {EAuthority} from "@model/enum/authority.enum";
import {AuthenticationService} from "@core/services";

@Component({
  selector: "app-user-details",
  templateUrl: "./role-details.component.html",
  styleUrls: ["./role-details.component.scss"],
  providers: [DatePipe]
})
export class RoleDetailsComponent implements OnInit {
  roleId: number;
  role: Role;
  roleDetailsForm: FormGroup;
  editMode: boolean;
  authorities: Authority[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private toastr: ToastService,
              private translate: TranslateService,
              private formBuilder: FormBuilder,
              private i18nService: I18nService,
              private roleService: RoleService,
              private authenticationService: AuthenticationService) {
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.roleDetailsForm.controls;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  loadAuthorities() {
    this.roleService.listAuthorities().subscribe(data => {
      this.authorities = [...data];
    });
  }

  ngOnInit() {
    //this.systemAdminUser = this.authenticationService.currentUser.authorities.some((authority: []) => authority['authority'] == UserRoles.CPM_ADMIN);
    this.editMode = false;
    this.loadAuthorities();
    this.initForm();
    combineLatest(this.route.params, this.route.queryParams).pipe(map(results => ({
      params: results[0].id,
      qParams: results[1]
    }))).subscribe(results => {
      this.roleId = +results.params; // (+) converts string 'id' to a number

      if (this.roleId) {
        // load user details
        this.roleService.find(this.roleId).subscribe(data => {
          if (data && data.id) {
            this.role = data;
            if (results.qParams.edit) {
              this.enableEditMode();
            }
            this.updateForm();
          } else {
            this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.roleId}),
              this.translate.instant('general.dialog_error_title'));
            this.goToList();
          }
        });
      } else {
        this.toastr.error(this.translate.instant('general.route_id_param_not_found'),
          this.translate.instant('general.dialog_error_title'));
        this.goToList();
      }
    });
  }

  goToList() {
    this.router.navigate(['/roles/list']);
  }

  enableEditMode() {
    this.editMode = true;
  }

  saveOrUpdate() {
    if (this.roleDetailsForm.invalid) {
      return;
    }
    this.updateUserModel();
    this.roleService.saveOrUpdate(this.role).subscribe(res => {
      if (res.hasOwnProperty('errors') && res.errors) {
        this.toastr.warning(this.translate.instant('general.dialog_form_error_text'),
          this.translate.instant('general.dialog_edit_title'));
        Object.keys(this.roleDetailsForm.controls).forEach(field => {
          console.log('looking for validation errors for : ' + field);
          if (res.errors[field]) {
            const control = this.roleDetailsForm.get(field);
            control.setErrors({invalid: res.errors[field].replace(/\{/, '').replace(/\}/, '')});
            control.markAsTouched({onlySelf: true});
          }
        });
      } else {
        this.role = res;
        this.enableViewMode();
        this.toastr.success(this.translate.instant('general.dialog_edit_success_text'),
          this.translate.instant('general.dialog_edit_title'));
      }
    });
  }

  private updateUserModel() {
    /*this.role.nin = this.roleDetailsForm.controls.nin.value;
    this.role.gender = this.roleDetailsForm.controls.gender.value;
    this.role.dateOfBirthGregorian = this.formatDate(this.roleDetailsForm.controls.dateOfBirthGregorian.value);
    this.role.email = this.roleDetailsForm.controls.email.value;
    this.role.familyName = this.roleDetailsForm.controls.familyName.value;
    this.role.fatherName = this.roleDetailsForm.controls.fatherName.value;
    this.role.firstName = this.roleDetailsForm.controls.firstName.value;
    this.role.grandFatherName = this.roleDetailsForm.controls.grandFatherName.value;
    this.role.mobileNumber = this.roleDetailsForm.controls.mobileNumber.value;
    // user cannot edit his region and roles
    if (!this.editHimSelf) {
      this.role.roles = [];
      this.originalRoles.forEach(role => {
        if (role.id === +this.roleDetailsForm.controls.roleId.value) {
          /!*const userRole = new UserRole();
          userRole.role = role;
          this.user.roles.push(userRole);*!/
        }
      });
    }*/
  }

  enableViewMode() {
    this.editMode = false;
  }

  private initForm() {
    this.roleDetailsForm = this.formBuilder.group({
      id: [''],
      nin: ['', Validators.required],
      gender: [''],
      mobileNumber: ['', Validators.required],
      firstName: ['', Validators.required],
      fatherName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      grandFatherName: [''],
      familyName: ['', Validators.required],
      dateOfBirthGregorian: ['', Validators.required],
      dateOfBirthHijri: [''],
      avatar: [''],
      roleId: ['', Validators.required]
    });
  }

  private updateForm() {
    /*this.roleDetailsForm = this.formBuilder.group({
      id: [this.role.id],
      nin: [this.role.nin, Validators.required],
      gender: [this.role.gender, Validators.compose([Validators.required, Validators.pattern('m|M|f|F{1}')])],
      mobileNumber: [this.role.mobileNumber, Validators.required],
      firstName: [this.role.firstName, Validators.required],
      fatherName: [this.role.fatherName],
      email: [this.role.email, [Validators.required, Validators.email]],
      grandFatherName: [this.role.grandFatherName],
      familyName: [this.role.familyName, Validators.required],
      dateOfBirthGregorian: [this.toCalender(this.role.dateOfBirthGregorian)],
      dateOfBirthHijri: [this.role.dateOfBirthHijri],
      avatar: [this.role.avatar],
      roleId: [this.role.role, Validators.required]
    });*/
  }

  resetUserRolesCheckboxes(event: any) {
    for (let role of this.authorities) {
      if (role['id'] == event.value) {

        break;
      }
    }
  }

  formatDate(date: any) {
    return date.year + '-' + ('00' + date.month).slice(-2) + '-' + ('00' + date.day).slice(-2);
  }

  get canSeeRoleDetails(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.ROLE_MANAGEMENT);
  }
}
