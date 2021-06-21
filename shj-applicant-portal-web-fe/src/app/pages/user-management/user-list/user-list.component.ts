import {Component, OnDestroy, OnInit, ViewEncapsulation} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AuthenticationService, UserService} from '@app/_core/services';
import {Page, User} from '@app/_shared/model';
import {Role} from '@app/_shared/model/role.model';
import {UserStatus} from '@model/user-status.model';
import {Subscription} from "rxjs";
import {I18nService} from "@dcc-commons-ng/services";
import {RoleService} from "@core/services/role/role.service";
import {TranslateService} from "@ngx-translate/core";
import {ToastService} from "@shared/components/toast";
import {ConfirmDialogService} from "@shared/components/confirm-dialog";
import {EAuthority} from "@model/enum/authority.enum";

@Component({
  selector: "app-user-list",
  encapsulation: ViewEncapsulation.None,
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.scss"],
})
export class UserListComponent implements OnInit, OnDestroy {
  ninPatt = /^[1-2]\d{9}$/;
  users: Array<User>;
  pageArray: Array<number>;
  page: Page;
  private listSubscription: Subscription;
  private searchSubscription: Subscription;
  private activateSubscription: Subscription;
  private deactivateSubscription: Subscription;
  private deleteSubscription: Subscription;
  loggedInUsername: any;
  ninToSearch: string;

  roles: Role[];
  userStatuses: UserStatus[];
  searchForm: FormGroup;

  canAddUser: boolean;
  canEditUser : boolean;
  canChangeUserStatus: boolean;
  canDeleteUser: boolean;
  canResetUserPassword: boolean;

  constructor(
    private userService: UserService,
    private i18nService: I18nService,
    private roleService: RoleService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translate: TranslateService,
    private toastr: ToastService,
    private confirmDialogService: ConfirmDialogService,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.loggedInUsername = this.authenticationService.currentUser.principal;
    this.canAddUser = this.authenticationService.hasAuthority(EAuthority.ADD_USER);
    this.loadPage(0);
    this.initForm();
    this.getRoles();
    this.initUserStatuses();
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

  get f() {
    return this.searchForm.controls;
  }

  private getRoles(): void {
    this.roleService.listAll().subscribe((data) => {
      this.roles = data;
    });
  }

  private initForm(): void {
    this.searchForm = this.formBuilder.group({
      status: [null],
      role: [null],
      nin: ['', [Validators.pattern(this.ninPatt)]]
    });
  }

  private initUserStatuses(): void {
    this.userStatuses = [new UserStatus(1, 'نشط', 'ِActivated', true), new UserStatus(2, 'غير نشط', 'Deactivated', false)];
  }

  loadPage(page: number) {
    // load users for param page
    this.listSubscription = this.userService.list(page).subscribe(data => {
      this.page = data;
      if (this.page != null) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.users = this.page.content;
        //check logged in user actions authorities
        this.canEditUser = this.authenticationService.hasAuthority(EAuthority.EDIT_USER);
        this.canChangeUserStatus = this.authenticationService.hasAuthority(EAuthority.CHANGE_USER_STATUS);
        this.canDeleteUser = this.authenticationService.hasAuthority(EAuthority.DELETE_USER);
        this.canResetUserPassword = this.authenticationService.hasAuthority(EAuthority.RESET_PASSWORD);
      }
    });
  }


  activateUser(userId: number) {
    this.confirmDialogService.confirm(this.translate.instant('user-management.dialog_activate_confirm_text'), this.translate.instant('general.dialog_confirmation_title')).then(confirm => {
      if (confirm) {
        this.activateSubscription = this.userService.activate(userId).subscribe(_ => {
          this.toastr.success(this.translate.instant('user-management.dialog_activate_success_text'), this.translate.instant('user-management.dialog_activate_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('user-management.dialog_activate_title'));
        });
      }
    });
  }

  deactivateUser(userId: number) {
    this.confirmDialogService.confirm(this.translate.instant('user-management.dialog_deactivate_confirm_text'), this.translate.instant('general.dialog_confirmation_title')).then(confirm => {
      if (confirm) {
        this.deactivateSubscription = this.userService.deactivate(userId).subscribe(_ => {
          this.toastr.success(this.translate.instant('user-management.dialog_deactivate_success_text'), this.translate.instant('user-management.dialog_deactivate_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('user-management.dialog_deactivate_title'));
        });
      }
    });
  }

  deleteUser(userId: number) {
    this.confirmDialogService.confirm(this.translate.instant('user-management.dialog_delete_confirm_text'), this.translate.instant('general.dialog_confirmation_title')).then(confirm => {
      if (confirm) {
        this.deleteSubscription = this.userService.delete(userId).subscribe(_ => {
          this.toastr.success(this.translate.instant('user-management.dialog_delete_success_text'), this.translate.instant('user-management.dialog_delete_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('user-management.dialog_delete_title'));
        });
      }
    });
  }

  resetUserPassword(userIdNumber: number) {
    this.confirmDialogService.confirm(this.translate.instant('user-management.dialog_reset_user_pass_confirm_text'), this.translate.instant('general.dialog_confirmation_title')).then(confirm => {
      if (confirm) {
        //TODO: call backend
        this.deleteSubscription = this.userService.resetUserPassword(userIdNumber).subscribe(_ => {
          this.toastr.success(this.translate.instant('user-management.dialog_reset_user_pass_success_text'), this.translate.instant('user-management.dialog_reset_user_pass_title'));
          this.loadPage(0);
        }, error => {
          this.toastr.error(this.translate.instant('general.dialog_error_text'), this.translate.instant('user-management.dialog_reset_user_pass_title'));
        });
      }
    });
  }

  onRoleChange(e: any) {
    if (this.searchForm.get("role").value != undefined) {
      if (this.searchForm.get("role").value && this.searchForm.get("role").value.id) {
        console.log(this.searchForm.value);
      }
    }
  }

  searchButtonEnabled() {
    Object.keys(this.searchForm.controls).forEach(field => {
      if (this.searchForm.get(field).valid) {
        return true;
      }
    });
    return false;
  }

  pageCounter(i: number): Array<number> {
    return new Array(i);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  public getUserFullName(user: User) {
    return `${user.firstName != null ? user.firstName : ''} ${user.fatherName != null ? user.fatherName : ''} ${
      user.grandFatherName != null ? user.grandFatherName : ''
    } ${user.familyName != null ? user.familyName : ''}`;
  }

  search(): void {
    this.searchSubscription = this.userService.search(0, this.searchForm.value.role,
      this.searchForm.value.nin, this.searchForm.value.status).subscribe(data => {
      this.users = [];
      this.pageArray = [];
      this.page = data;
      console.log(data);
      if (this.page != null && this.page.totalElements) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.users = this.page.content;
      } else {
        this.page = null;
        this.toastr.info(this.translate.instant('general.table_emtpy_text'), this.translate.instant('general.search'));
    }
    });
  }

  cancelSearch() {
    this.loadPage(0);
    this.initForm();
  }

  get canSeeUsersList(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.USER_MANAGEMENT);
  }

  userMainRole(user: User) : Role {
    if (user) {
      let userRole = user.userRoles.find(userRole => userRole.mainRole);
      if (userRole) return userRole.role;
    }
    return null;
  }
}
