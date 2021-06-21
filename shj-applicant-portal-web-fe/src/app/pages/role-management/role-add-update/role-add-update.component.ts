import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Role} from '@app/_shared/model/role.model';
import {ToastService} from '@shared/components/toast/toast-service';
import {Authority} from "@model/authority.model";
import {RoleService} from "@core/services/role/role.service";
import {I18nService} from "@dcc-commons-ng/services";
import {RoleAuthority} from "@model/role-authority.model";
import {TranslateService} from "@ngx-translate/core";
import {EAuthority} from "@model/enum/authority.enum";
import {AuthenticationService} from "@core/services";
import {DccValidators} from "@shared/validators";

@Component({
  selector: 'app-add-update-role',
  templateUrl: './role-add-update.component.html',
  styleUrls: ['./role-add-update.component.scss']
})
export class RoleAddUpdateComponent implements OnInit {

  role: Role = new Role();
  retrievedRole: Role;
  roleAuthorities: RoleAuthority[] = [];
  selectedAuthorities: Authority[] = [];

  roleForm: FormGroup;


  constructor(private formBuilder: FormBuilder,
              private roleService: RoleService,
              private i18nService: I18nService,
              private router: Router,
              private activeRoute: ActivatedRoute,
              private translate: TranslateService,
              private toastr: ToastService,
              private authenticationService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.initRoleForm();
    this.loadAuthorities();
  }

  loadAuthorities() {
    this.roleService.listAuthorities().subscribe(data => {
      this.roleAuthorities = [];
      if(data) {
        data.forEach(authority => {
          this.addRoleAuthority(authority);
          if(authority.children) {
            authority.children.forEach(childAuthority => this.addRoleAuthority(childAuthority));
          }
        });
      }
      let roleId = this.activeRoute.snapshot.params.id;
      if (roleId) {
        this.roleService.find(roleId).subscribe(role => {
          this.retrievedRole = role;
          this.role = role;
          this.updateRoleForm();
        });
      }
    });
  }

  addRoleAuthority(authority: Authority) {
    let ra: RoleAuthority = new RoleAuthority();
    ra.authority = authority;
    this.roleAuthorities.push(ra);
  }

  get f() {
    return this.roleForm.controls;
  }

  initRoleForm() {
    this.roleForm = this.formBuilder.group({
      nameArabic: ['', [Validators.required, DccValidators.arabicCharacters(false), Validators.maxLength(60), Validators.minLength(3)]],
      nameEnglish: ['', [Validators.required, DccValidators.latinCharacters(false), Validators.maxLength(60), Validators.minLength(3)]],
      activated: [false, Validators.required]
    });
  }

  updateRoleForm() {
    this.roleForm = this.formBuilder.group({
      id: [this.role.id],
      nameArabic: [this.role.nameArabic, [Validators.required, Validators.maxLength(60), Validators.minLength(3)]],
      nameEnglish: [this.role.nameEnglish, [Validators.required, Validators.maxLength(60), Validators.minLength(3)]],
      activated: [this.role.activated, Validators.required]
    });
    this.roleAuthorities.forEach(ra => {
      if (this.role.roleAuthorities.filter(roleAuthority => roleAuthority.authority.id === ra.authority.id).length > 0) {
        ra.selected = true;
      }
    });
  }

  goToList() {
    this.router.navigate(['/roles/list']);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  saveOrUpdateRole() {

    Object.keys(this.roleForm.controls).forEach(field => {
      const control = this.roleForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    if (this.roleForm.invalid) {
      return;
    }
    this.role = this.roleForm.value;
    this.role.roleAuthorities = this.roleAuthorities.filter(ra => ra.selected);
    // adding parent to if any child is selected
    this.role.roleAuthorities.forEach(selectedChild => {
      let parent:RoleAuthority = this.roleAuthorities.find(raParent => raParent.authority.id === selectedChild.authority.parentId);
      if(parent && !this.role.roleAuthorities.find(ra => ra.authority.id == parent.authority.id)) {
        this.role.roleAuthorities.push(parent);
      }
    });
    // removing parent to if none of its children is selected
    this.roleAuthorities.filter(ra => ra.authority.parentId === null).forEach(parent => {
      if (parent.authority.code !== EAuthority.ADMIN_DASHBOARD && this.role.roleAuthorities.find(raSelected => parent.authority.id == raSelected.authority.id) &&
      !parent.authority.children.filter(ra => this.role.roleAuthorities.find(raSelected => ra.id == raSelected.authority.id)).length) {
        this.role.roleAuthorities = this.role.roleAuthorities.filter(raSelected => raSelected.authority.id !== parent.authority.id);
      }
    });
    if(this.role.roleAuthorities.length == 0) {
      this.toastr.warning(this.translate.instant('role-management.add_role_no_authorities_error'), this.translate.instant('general.dialog_alert_title'));
      return;
    }

    if(this.retrievedRole) {
      //fields that should not be changed while edit
      this.role.creationDate = this.retrievedRole.creationDate;
    }

    this.roleService.saveOrUpdate(this.role).subscribe(res => {
      if (res.hasOwnProperty('errors') && res.errors) {
        this.toastr.warning(this.translate.instant('general.dialog_form_error_text'), this.translate.instant('general.dialog_edit_title'));
        Object.keys(this.roleForm.controls).forEach(field => {
          console.log('looking for validation errors for : ' + field);
          if (res.errors[field]) {
            const control = this.roleForm.get(field);
            control.setErrors({invalid: res.errors[field].replace(/\{/, '').replace(/\}/, '')});
            control.markAsTouched({onlySelf: true});
          }
        });
      } else {
        this.toastr.success(this.translate.instant('general.dialog_edit_success_text'), this.translate.instant('general.dialog_edit_title'));
        this.goToList();
      }
    });
  }

  toggleAuthority(roleAuthority: RoleAuthority) {
    roleAuthority.selected = !roleAuthority.selected;
  }

  get canSeeAddUpdateRole(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.ADD_ROLE) || this.authenticationService.hasAuthority(EAuthority.EDIT_ROLE);
  }
}
