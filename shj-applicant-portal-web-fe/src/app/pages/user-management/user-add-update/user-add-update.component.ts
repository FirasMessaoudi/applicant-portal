import {Component, OnInit, ViewChild} from '@angular/core';
import {User, UserRole} from "@shared/model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Role} from "@model/role.model";
import {AuthenticationService, DEFAULT_MAX_USER_AGE, UserService} from "@core/services";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {RoleService} from "@core/services/role/role.service";
import {I18nService} from "@dcc-commons-ng/services";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/datepicker/date-formatter.service";
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/datepicker/consts";
import {HijriGregorianDatepickerComponent} from "@shared/modules/hijri-gregorian-datepicker/datepicker/hijri-gregorian-datepicker.component";
import {EAuthority} from "@model/enum/authority.enum";
import {DccValidators, IdType} from "@shared/validators";
import {IDropdownSettings} from "ng-multiselect-dropdown/multiselect.model";


@Component({
  selector: 'app-user-add-update',
  templateUrl: './user-add-update.component.html',
  styleUrls: ['./user-add-update.component.scss']
})
export class UserAddUpdateComponent implements OnInit {

  url: any = 'assets/images/default-avatar.svg';
  file: File;

  user: User = new User();
  roles: Role[];
  mainSelectedRole: Role;
  additionalRoles: Role[];
  userForm: FormGroup;

  selectedDateOfBirth: NgbDateStruct;
  maxDateOfBirthGregorian: NgbDateStruct;
  maxDateOfBirthHijri: NgbDateStruct;
  dateString: string;
  selectedDateType: any;

  additionalSelectedRoles = [];
  dropdownSettings:IDropdownSettings = {};//TODO: check if it can be defined at the app level.

  @ViewChild('datePicker') dateOfBirthPicker: HijriGregorianDatepickerComponent;


  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private roleService: RoleService,
              private i18nService: I18nService,
              private router: Router,
              private activeRoute: ActivatedRoute,
              private authenticationService: AuthenticationService,
              private translate: TranslateService,
              private toastr: ToastService,
              private dateFormatterService: DateFormatterService) {
  }


  ngOnInit(): void {

    //TODO: use localized labels for select all and unselect all.
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: this.currentLanguage.startsWith("en") ? 'nameEnglish' : 'nameArabic',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: false
    };

    this.roleService.listActive().subscribe(data => {
      this.roles = data;
    });

    this.additionalSelectedRoles = [];

    // calendar default;
    let toDayGregorian = this.dateFormatterService.todayGregorian();
    let toDayHijri = this.dateFormatterService.todayHijri();
    this.maxDateOfBirthGregorian = {
      year: toDayGregorian.year - DEFAULT_MAX_USER_AGE,
      month: toDayGregorian.month,
      day: toDayGregorian.day
    };
    this.maxDateOfBirthHijri = {
      year: toDayHijri.year - DEFAULT_MAX_USER_AGE,
      month: toDayHijri.month,
      day: toDayHijri.day
    };
    this.selectedDateType = DateType.Gregorian;

    this.initUserForm();

    let userId = this.activeRoute.snapshot.params.id;
    if (userId) {
      this.userService.find(userId).subscribe(user => {
        this.user = user;
        this.user.userRoles.forEach(userRole => {
          if (userRole.mainRole) {
            this.mainSelectedRole = userRole.role;
          } else {
            this.additionalSelectedRoles.push(userRole.role);
          }
        });
        if (this.user.dateOfBirthGregorian) {
          this.selectedDateOfBirth = this.dateFormatterService.fromDate(this.user.dateOfBirthGregorian);
          this.dateString = this.dateFormatterService.toString(this.dateFormatterService.toHijri(this.selectedDateOfBirth));
        }
        this.updateUserForm();
      });
    }
  }

  get f() {
    return this.userForm.controls;
  }

  initUserForm() {
    this.userForm = this.formBuilder.group({
      id: [-1, Validators.required],
      mobileNumber: ['', [DccValidators.mobileNumber(), Validators.required]],
      nin: ['', [DccValidators.ninOrIqama(IdType.NIN_OR_IQAMA), Validators.required]],
      gender: ['M', Validators.required],
      dateOfBirthGregorian: ['' , Validators.required],
      dateOfBirthHijri: ['' , Validators.required],
      email: ['' , Validators.required],
      familyName: ['' , [DccValidators.charactersOnly(), Validators.required]],
      firstName: ['' , [DccValidators.charactersOnly(), Validators.required]],
      grandFatherName: ['', DccValidators.charactersOnly()],
      fatherName: ['', DccValidators.charactersOnly()],
      activated: [false, Validators.required],
      role: [null, Validators.required],
      additionalRoles: [[]],
      userRoles: [null]
    });
  }

  onMainRoleChange(selectedRole: any) {
    this.mainSelectedRole = selectedRole;
    this.additionalRoles = [];
    this.roles.forEach(role => {
      if (role.id != selectedRole.id) {
        this.additionalRoles.push(role);
      }
    });
  }

  updateUserForm() {
    this.userForm = this.formBuilder.group({
      id: [this.user.id, Validators.required],
      mobileNumber: [this.user.mobileNumber, [DccValidators.mobileNumber(), Validators.required]],
      nin: [this.user.nin, [DccValidators.ninOrIqama(IdType.NIN_OR_IQAMA), Validators.required]],
      gender: [this.user.gender ? this.user.gender : 'M', Validators.required],
      dateOfBirthGregorian: [this.user.dateOfBirthGregorian, Validators.required],
      dateOfBirthHijri: [this.user.dateOfBirthHijri, Validators.required],
      email: [this.user.email, Validators.required],
      familyName: [this.user.familyName, [DccValidators.charactersOnly(), Validators.required]],
      firstName: [this.user.firstName,  [DccValidators.charactersOnly(), Validators.required]],
      grandFatherName: [this.user.grandFatherName, DccValidators.charactersOnly()],
      fatherName: [this.user.fatherName, DccValidators.charactersOnly()],
      activated: [this.user.activated, Validators.required],
      role: [this.mainSelectedRole, Validators.required],
      userRoles: [this.user.userRoles, Validators.required]
    });
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  public getUserFullName() {
    return `${this.user.firstName != null ? this.user.firstName : ""} ${this.user.fatherName != null ? this.user.fatherName : ""} ${
      this.user.grandFatherName != null ? this.user.grandFatherName : ""
    } ${this.user.familyName != null ? this.user.familyName : ""}`;
  }

  loadAvatar(event: any) {
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]); // read file as data url
      reader.onload = () => { // called once readAsDataURL is completed
        this.url = reader.result;
        this.file = event.target.files[0];
        this.userService.uploadAvatar(this.file, this.user.id).subscribe(res => {
          if (res.hasOwnProperty("errors") && res.errors) {
            this.toastr.warning(this.translate.instant("general.dialog_file_upload_error_text"), this.translate.instant("general.dialog_error_title"));
          } else {
            this.user.avatar = res.text;
            this.toastr.success(this.translate.instant("general.dialog_file_upload_success_text"), this.translate.instant("general.dialog_edit_title"));
          }
        });
      }
    }
  }

  createUserRole(role: Role, mainRole: boolean): UserRole {
    let userRole: UserRole = new UserRole();
    userRole.user = this.user;
    userRole.role = role;
    userRole.mainRole = mainRole;
    return userRole;
  }

  saveOrUpdate() {

    Object.keys(this.userForm.controls).forEach(field => {
      const control = this.userForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    if (this.userForm.invalid) {
      return;
    }

    let userRoles = [];
    // create UserRole for the main selected role and additional roles (if any).
    userRoles.push(this.createUserRole(this.mainSelectedRole, true));
    this.userForm.controls.additionalRoles.value.forEach(role => {
      userRoles.push(this.createUserRole(role, false));
    });
    this.f.userRoles.setValue(userRoles);

    this.userService.saveOrUpdate(this.userForm.value).subscribe(res => {
      if (res.hasOwnProperty("errors") && res.errors) {
        this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("general.dialog_edit_title"));
        Object.keys(this.userForm.controls).forEach(field => {
          console.log("looking for validation errors for : " + field);
          if (res.errors[field]) {
            const control = this.userForm.get(field);
            control.setErrors({invalid: res.errors[field].replace(/\{/, '').replace(/\}/, '')});
            control.markAsTouched({onlySelf: true});
          }
        });
      } else {
        this.user = res;
        this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));
        this.goToList();
      }
    });
  }

  goToList() {
    this.router.navigate(['/users/list']);
  }

  onDateOfBirthChange(event) {
    if(event) {
      let dateStruct = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : this.dateFormatterService.toGregorian(event);
      let dateStructGreg = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? event : this.dateFormatterService.toGregorian(event);
      let dateStructHijri = this.dateOfBirthPicker.selectedDateType == DateType.Gregorian ? this.dateFormatterService.toHijri(event) : event;
      this.dateString = this.dateFormatterService.toString(dateStruct);
      this.userForm.controls.dateOfBirthGregorian.setValue(this.dateFormatterService.toDate(dateStructGreg));
      this.userForm.controls.dateOfBirthHijri.setValue(this.dateFormatterService.toString(dateStructHijri).split('/').reverse().join(''));
    }
  }

  get canSeeAddUpdateUser(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.ADD_USER) || this.authenticationService.hasAuthority(EAuthority.EDIT_USER);
  }
}
