import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Page} from '@app/_shared/model';
import {RoleService} from "@core/services/role/role.service";
import {Subscription} from "rxjs";
import {Role} from "@model/role.model";
import {I18nService} from "@dcc-commons-ng/services";
import {AuthenticationService} from "@core/services";
import {ToastService} from "@shared/components/toast/toast-service";
import {TranslateService} from "@ngx-translate/core";
import {ConfirmDialogService} from "@shared/components/confirm-dialog";
import {EAuthority} from "@model/enum/authority.enum";

@Component({
  selector: 'app-applicant-list',
  templateUrl: './role-list.component.html',
  styleUrls: ['./role-list.component.scss']
})
export class RoleListComponent implements OnInit, OnDestroy {
  roles: Array<Role>;
  pageArray: Array<number>;
  page: Page;
  private listSubscription: Subscription;
  private searchSubscription: Subscription;
  private deleteSubscription: Subscription;
  private activateSubscription: Subscription;
  private deactivateSubscription: Subscription;

  authorities: any[];
  searchForm: FormGroup;
  loggedInUserRoleId: number;

  canAddRole: boolean;
  canEditRole : boolean;
  canChangeRoleStatus: boolean;
  canDeleteRole: boolean;

  constructor(
    private i18nService: I18nService,
    private roleService: RoleService,
    private formBuilder: FormBuilder,
    private toastr: ToastService,
    private translate: TranslateService,
    private confirmDialogService: ConfirmDialogService,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.loggedInUserRoleId = this.authenticationService.currentUser.role?.id;
    this.canAddRole = this.authenticationService.hasAuthority(EAuthority.ADD_ROLE);
    this.loadPage(0);
    this.initForm();
    this.getAuthorities();
  }

  ngOnDestroy() {
    if (this.listSubscription) {
      this.listSubscription.unsubscribe();
    }
    if (this.activateSubscription) {
      this.activateSubscription.unsubscribe();
    }
    if (this.deactivateSubscription) {
      this.deactivateSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }

  private getAuthorities(): void {
    this.roleService.listAuthorities().subscribe(data => {
      this.authorities = [...data];
    });
  }

  private initForm(): void {
    this.searchForm = this.formBuilder.group({
      authorityId: [-1],
      roleNameAr: [null],
      roleNameEn: [null]
    });
  }

  loadPage(page: number) {
    // load roles for param page
    this.listSubscription = this.roleService.listPaginated(page).subscribe(data => {
      this.page = data;
      if (this.page != null) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.roles = this.page.content;
        //check logged in user actions authorities
        this.canEditRole = this.authenticationService.hasAuthority(EAuthority.EDIT_ROLE);
        this.canChangeRoleStatus = this.authenticationService.hasAuthority(EAuthority.CHANGE_ROLE_STATUS);
        this.canDeleteRole = this.authenticationService.hasAuthority(EAuthority.DELETE_ROLE);
      }
    });
  }

  pageCounter(i: number): Array<number> {
    return new Array(i);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  search() {

    this.searchSubscription = this.roleService.search(this.searchForm.value.authorityId,
      this.searchForm.value.roleNameAr, this.searchForm.value.roleNameEn).subscribe(data => {
      this.roles = [];
      this.pageArray = [];
      this.page = data;
      if (this.page != null) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.roles = this.page.content;
      }
    });
  }

  cancelSearch() {
    this.loadPage(0);
    this.initForm();
  }

  deleteRole(roleId: number) {
    this.confirmDialogService.confirm('role-management.dialog_delete_confirm_text', 'role-management.dialog_delete_title').then(confirm => {
      if (confirm) {
        this.deleteSubscription = this.roleService.delete(roleId).subscribe(_ => {
          this.toastr.success(this.translate.instant('role-management.dialog_delete_success_text'), this.translate.instant('role-management.dialog_delete_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('role-management.dialog_delete_title'));
        });
      }
    });
  }

  activateRole(roleId: number) {
    this.confirmDialogService.confirm('role-management.dialog_activate_confirm_text', 'role-management.dialog_activate_title').then(confirm => {
      if (confirm) {
        this.activateSubscription = this.roleService.activate(roleId).subscribe(_ => {
          this.toastr.success(this.translate.instant('role-management.dialog_activate_success_text'), this.translate.instant('role-management.dialog_activate_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('role-management.dialog_activate_title'));
        });
      }
    });
  }

  deactivateRole(roleId: number) {
    this.confirmDialogService.confirm('role-management.dialog_deactivate_confirm_text', 'role-management.dialog_deactivate_title').then(confirm => {
      if (confirm) {
        this.deactivateSubscription = this.roleService.deactivate(roleId).subscribe(_ => {
          this.toastr.success(this.translate.instant('role-management.dialog_deactivate_success_text'), this.translate.instant('role-management.dialog_deactivate_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('role-management.dialog_deactivate_title'));
        });
      }
    });
  }

  get canSeeRoleList(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.ROLE_MANAGEMENT);
  }
}
