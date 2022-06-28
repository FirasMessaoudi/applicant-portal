import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgbDropdown, NgbModal, NgbTypeahead} from '@ng-bootstrap/ng-bootstrap';
import {AuthenticationService, CardService, UserService} from "@core/services";
import {LookupService} from "@core/utilities/lookup.service";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DccValidators} from "@shared/validators";
import {UserContacts} from "@model/UserContacts.model";
import {Router} from "@angular/router";
import {User} from "@shared/model";
import {I18nService} from "@dcc-commons-ng/services";
import {merge, Observable, Subject} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {Lookup} from "@model/lookup.model";
import {ConfirmDialogService} from "@shared/components/confirm-dialog";
import {NotificationService} from '@app/_core/services/notification/notification.service';
import {NotificationCategory} from '@app/_shared/model/notification-category.model';
import {UserNotificationCategoryPreference} from '@app/_shared/model/user-notification-category-preference.model';
import {EmergencyData} from "@model/emergency-data.model";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  seasons: number [] = [];
  applicantRituals: ApplicantRitualLite [] = [];
  selectedSeason: number;
  selectedApplicantRitualPackage: ApplicantRitualLite;
  ritualTypes: Lookup[] = [];
  notificationsList: any[] = [];
  notificationsListLookup: any[] = [];
  notificationsListPreference: UserNotificationCategoryPreference[] = [];
  preferenceIsLoaded: boolean = false;
  enableEditLanguage = false;
  selectedLanguage = "";
  contactsForm: FormGroup;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;
  userContacts: UserContacts = new UserContacts();
  countries: any;
  selectedCountryCode;
  selectedCountryName;
  selectedCountryPrefix: any;
  originalCountryPrefix: any;
  @ViewChild('instance')
  instance: NgbTypeahead;
  @ViewChild('elem')
  elem: ElementRef;
  @ViewChild('countryListDropdown')
  countryListDropdown: NgbDropdown;
  @ViewChild('countryListDropdown2')
  countryListDropdown2: NgbDropdown;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  emergencyDataForm: FormGroup;
  emergencyDataOriginalContactName: any;
  emergencyDataOriginalMobileNo: any;
  emergencyDataOriginalCountryCode: any;
  emergencyDataOriginalCountryPrefix: any;
  emergencyDataSelectedCountryCode='SA';
  emergencyDataSelectedCountryPrefix='+966';
  emergencyDataSelectedCountryNamePrefix='SA';
  emergencyData: EmergencyData = new EmergencyData();

  /*   notificationsList = [
      {
        id: 0,
        checked: true,
        title: 'عامة',
        icon: 'comment-alt-lines-light',
        iconColor: 'dcc-primary',
        description: 'احرص على حمل بطاقة الحج الخاصة بك عند اداء الشعائر'
      },
      {
        id: 1,
        checked: false,
        title: 'صحي',
        icon: 'heartbeat-light',
        iconColor: 'dcc-danger',
        description: 'في حالة ارتفاع درجة حرارتك فوق 38 درجة توجة الى اقرب نقطة صحية مباشرة'
      },
      {
        id: 2,
        checked: true,
        title: 'شعيرة',
        icon: 'flag-light',
        iconColor: 'dcc-primary',
        description: 'طواف الإفاضة هو رُكن من أركان الحجّ لا يتمّ الحج إلّا بالإتيان به؛ ودليل ذلك قوله -تعالى-: (وَلْيَطَّوَّفُوا بِالْبَيْتِ الْعَتِيقِ)'
      },
      {
        id: 3,
        checked: false,
        title: 'توعية عامة',
        icon: 'bullhorn-light',
        iconColor: 'dcc-blue',
        description: 'تجنب صعود الجبال والاماكن المرتفعة وتجنب المزاحمة والالتحام والافتراش في الطرقات'
      },
      {
        id: 0,
        checked: false,
        title: 'دينية',
        icon: 'kaaba-light',
        iconColor: 'dcc-primary',
        description: 'ربنا تقبل منا إنك أنت السميع العليم.'
      },

    ] */

  constructor(private modalService: NgbModal,
              private userService: UserService,
              private cardService: CardService,
              private i18nService: I18nService,
              private toastr: ToastService,
              private formBuilder: FormBuilder,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private lookupsService: LookupService,
              private elRef: ElementRef,
              private notificationService: NotificationService,
              private confirmDialogService: ConfirmDialogService,
              private router: Router) {
  }

  ngOnInit() {

    this.loadLookups();
    this.loadUserNotificationCategory();
    this.selectedLanguage = this.currentLanguage;
    this.userService.find(this.authenticationService.currentUser?.id).subscribe(data => {
      if (data && data.id) {
        this.contactsForm.controls['email'].setValue(data.email);
        this.contactsForm.controls['mobileNumber'].setValue(data.mobileNumber);
        this.originalMobileNo = data.mobileNumber
        this.originalEmail = data.email;
        this.originalCountryCode = data?.countryCode?.toLowerCase();
        this.selectedCountryCode = data?.countryCode?.toLowerCase();
        this.originalCountryPrefix = '+' + data.countryPhonePrefix;
        this.selectedCountryPrefix = '+' + data.countryPhonePrefix;
        this.selectedCountryName = this.countries.find(country=>country.countryPhonePrefix== this.selectedCountryPrefix).countryNamePrefix;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));
      }
    });

    this.userService.findApplicantEmergencyContact().subscribe(data => {
      if (data) {
        console.log(data);
        this.emergencyDataForm.controls['emergencyContactName'].setValue(data.body.emergencyContactName);
          this.emergencyDataForm.controls['emergencyContactMobileNumber'].setValue(data?.body?.emergencyContactMobileNumber?.substring(1));
        this.emergencyDataOriginalMobileNo = data?.body?.emergencyContactMobileNumber;
        this.emergencyDataOriginalCountryPrefix = data?.body?.emergencyContactMobileNumber;
      /*  this.emergencyDataOriginalContactName = data.email;
        this.emergencyDataOriginalCountryCode = data?.countryCode?.toLowerCase();
        this.emergencyDataSelectedCountryCode = data?.countryCode?.toLowerCase();
        this.emergencyDataSelectedCountryNamePrefix = data?.countryCode?.toLowerCase();
        if(data.body.emergencyContactMobileNumber!=null) {
          this.emergencyDataOriginalCountryPrefix = data?.body?.emergencyContactMobileNumber;
          //this.emergencyDataSelectedCountryPrefix = data?.body?.emergencyContactMobileNumber;
        }*/
      } else {

        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));
      }
    });

    this.createForm();
    this.createEmergencyDataForm();
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  updateUserLanguage() {
    this.enableEditLanguage = true;
    if (this.selectedLanguage != "" && this.selectedLanguage != this.currentLanguage) {
      this.setLanguage(this.selectedLanguage);
      this.userService.updatePreferredLang(this.selectedLanguage?.startsWith('ar') ? "ar" : "en", this.authenticationService.currentUser.principal).subscribe(response => {
        if (response && response.errors) {
          this.toastr.warning(this.translate.instant("general.dialog_error_text"), this.translate.instant("general.dialog_edit_title"));
        } else {
          this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));
        }
      });
    }
    this.enableEditLanguage = false;
  }

  private createForm() {
    this.contactsForm = this.formBuilder.group({
      mobileNumber: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [DccValidators.email, Validators.required]],
    });
  }

  private createEmergencyDataForm() {
    this.emergencyDataForm = this.formBuilder.group({
      emergencyContactName: ['', []],
      emergencyContactMobileNumber: ['', [Validators.maxLength(20),Validators.pattern("^[0-9]*$")]],
    });
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      result.forEach(c => c.countryPhonePrefix = '+' + c.countryPhonePrefix);
      this.countries = result;
    });
  }

  async loadNotificationCategory() {
    this.notificationService.loadNotificationCategoryLookup().subscribe(result => {
      this.notificationsListLookup = result;

    })
  }

  async loadNotificationCaotegoryPreference() {
    this.notificationService.loadNotificationCategoryPreference().subscribe(result => {
      if (result.length == this.lookupService().localizedItemsByLang(this.notificationsListLookup).length) {
        this.preferenceIsLoaded = true;
        this.notificationsListPreference = result;
      }
    })
  }

  async updateNotificationCaotegoryPreference() {

  }

  updateUserNotificationCategory(event, code: string) {

    if (this.preferenceIsLoaded) {
      let notificationCategory: UserNotificationCategoryPreference = this.notificationsListPreference.find((e) => e.categoryCode == code);
      notificationCategory.enabled = event.target.checked;

      this.notificationService.updateNotificationCategory(notificationCategory).subscribe(result => {
        console.log(result);
      })
    } else {
      this.lookupService().localizedItemsByLang(this.notificationsListLookup).forEach(value => {
        this.notificationService.updateNotificationCategory(new UserNotificationCategoryPreference(value.code, value.code == code ? event.target.checked : true)).subscribe(result => {
          console.log(result);
        });
      });
      this.loadNotificationCaotegoryPreference();
    }
  }

  async loadUserNotificationCategory() {
    await this.loadNotificationCategory();
    await this.loadNotificationCaotegoryPreference();
  }

  getCategoryIcon(code: string) {
    switch (code) {
      case 'GENERAL':
        return 'comment-alt-lines-light';
      case 'HEALTH':
        return 'heartbeat-light';
      case 'RELIGIOUS':
        return 'kaaba-light';
      case 'RITUAL':
        return 'flag-light';
      case 'GENERAL_AWARENESS':
        return 'bullhorn-light';
    }
  }

  getCategoryColor(code: string) {
    switch (code) {
      case 'GENERAL':
        return 'dcc-primary';
      case 'HEALTH':
        return 'dcc-danger';
      case 'RELIGIOUS':
        return 'dcc-primary';
      case 'RITUAL':
        return 'dcc-primary';
      case 'GENERAL_AWARENESS':
        return 'dcc-blue';
    }
  }

  filterNotificationCategory(list: NotificationCategory[]) {
    return list.filter(e => this.i18nService.language.startsWith(e.lang));
  }


  // convenience getter for easy access to form fields
  get f() {
    return this.contactsForm?.controls;
  }

  get emergencyDataFormControls() {
    return this.emergencyDataForm?.controls;
  }

  onSubmit() {
    // trigger all validations
    Object.keys(this.contactsForm.controls).forEach(field => {
      const control = this.contactsForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.contactsForm.invalid) {
      return;
    }
    if (this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.replace(/\s/g, "") ||
      this.originalEmail != this.contactsForm.controls['email'].value ||
      this.originalCountryCode.toUpperCase() != this.selectedCountryCode.toUpperCase()) {


      let reg1 = / /g;
      let reg2 = /\+/gi;
      let reg3 = /\-/gi;

      this.userContacts.countryCode = this.selectedCountryCode.toUpperCase();
      this.userContacts.email = this.contactsForm.controls['email'].value;
      this.userContacts.mobileNumber = this.contactsForm.controls['mobileNumber'].value.replace(reg1, "").replace(reg3, "");
      this.userContacts.countryPhonePrefix = this.selectedCountryPrefix.replace(reg2, "");

      this.userService.generateOTPForEditContact(this.userContacts).subscribe(response => {
        if (!response) {
          this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("general.dialog_edit_title"));
        } else {
          let user: User = new User();

          user.otpExpiryMinutes = response.otpExpiryMinutes;
          user.maskedMobileNumber = response.mobileNumber;

          this.authenticationService.updateOtpSubject({
            user: user,
            actionType: "/settings",
            editContacts: this.userContacts
          });
          this.router.navigate(['/edit/contacts/otp'], {replaceUrl: true});
        }
      })

    }
  }

  onEmergencyDataFormSubmit() {
    // trigger all validations
    Object.keys(this.emergencyDataForm.controls).forEach(field => {
      const control = this.emergencyDataForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.emergencyDataForm.invalid) {
      return;
    }
    if (this.emergencyDataOriginalMobileNo != this.emergencyDataForm.controls['emergencyContactMobileNumber'].value.replace(/\s/g, "") ||
      this.emergencyDataOriginalContactName != this.emergencyDataForm.controls['emergencyContactName'].value ||
      this.emergencyDataOriginalCountryCode.toUpperCase() != this.emergencyDataSelectedCountryCode.toUpperCase()) {


      let reg1 = / /g;
      let reg2 = /\+/gi;
      let reg3 = /\-/gi;


      this.emergencyData.emergencyContactName = this.emergencyDataForm.controls['emergencyContactName'].value;
      this.emergencyData.emergencyContactMobileNumber = this.emergencyDataSelectedCountryPrefix+ this.emergencyDataForm.controls['emergencyContactMobileNumber'].value.replace(reg1, "").replace(reg3, "");

        if (this.emergencyData?.emergencyContactName?.trim() === '' || this.emergencyData?.emergencyContactMobileNumber?.trim() === '') {
          this.toastr.warning(this.translate.instant("emergency_data.update_fail_empty"), this.translate.instant("general.dialog_error_text"));
        } else {
          this.userService.updateApplicantEmergencyContact(this.emergencyData).subscribe(response => {
            if (response && response.errors) {
              this.toastr.warning(this.translate.instant("general.dialog_error_text"), this.translate.instant("general.dialog_edit_title"));
            } else {
              this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));
            }
          })
        }



    }
  }

  public openTypeahead(): void {
    // Dispatch event on input element that NgbTypeahead is bound to
    this.elem.nativeElement.dispatchEvent(new Event('input'));
    // Ensure input has focus so the user can start typing
    setTimeout(() => { // this will make the execution after the above boolean has changed
      this.elem.nativeElement.focus();
    }, 0);
  }

  search = (text$: Observable<string>) => {
    const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
    const inputFocus$ = this.focus$;
    return merge(text$, inputFocus$, clicksWithClosedPopup$).pipe(
      map(term => (term === '' ? this.countries.filter(c => c.lang.toLowerCase() === this.i18nService.language.substr(0, 2))
        : this.countries.filter(c => c.lang.toLowerCase() === this.i18nService.language.substr(0, 2)).filter(v => v.label.toLowerCase().indexOf(term.toLowerCase()) > -1 || v.countryPhonePrefix.toLowerCase().indexOf(term) > -1
        ))));
  }

  inputFormatter = (x: { countryPhonePrefix: string }) => x.countryPhonePrefix

  onSelect($event, input) {
    $event.preventDefault();
    this.selectedCountryPrefix = $event.item.countryPhonePrefix;
    this.selectedCountryCode = $event.item.code.toLowerCase();
    this.selectedCountryName = $event.item.countryNamePrefix;
    this.countryListDropdown.close();
  }

  onSelectEmergencyDataSelectedCountryPrefix($event, input) {
    $event.preventDefault();
    this.emergencyDataSelectedCountryPrefix = $event.item.countryPhonePrefix;
    this.emergencyDataSelectedCountryCode = $event.item.code.toLowerCase();
    this.emergencyDataSelectedCountryNamePrefix = $event.item.countryNamePrefix.toLowerCase();

    this.countryListDropdown2.close();
  }

  onOpenChange(event: boolean) {
    if (!event) {
      this.elem.nativeElement.value = '';
    }
  }

  finalSave() {
    this.confirmDialogService.confirm('settings.dialog_save_confirm_text', 'general.dialog_edit_title').then(confirm => {
      if (confirm) {
        this.updateUserLanguage();
        this.onEmergencyDataFormSubmit();
        this.onSubmit();
      }
    });
  }
}
