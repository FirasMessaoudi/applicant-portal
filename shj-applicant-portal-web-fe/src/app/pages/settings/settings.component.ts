import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ModalDismissReasons, NgbModal, NgbTypeahead} from '@ng-bootstrap/ng-bootstrap';
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {AuthenticationService, CardService, UserService} from "@core/services";
import {Lookup} from "@model/lookup.model";
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

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SettingsComponent implements OnInit, AfterViewInit, OnDestroy {
  closeResult = '';

  SAUDI_MOBILE_NUMBER_REGEX: RegExp = new RegExp("^(009665|9665|\\+9665|05|5)([0-9]{8})$");

  seasons: number [] = [];
  applicantRituals: ApplicantRitualLite [] = [];
  selectedSeason: number;
  selectedApplicantRitual: ApplicantRitualLite;
  ritualTypes: Lookup[] = [];
  enableEditRitual = false;
  enableEditLanguage = true;
  selectedLanguage = "";
  contactsForm: FormGroup;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;
  userContacts: UserContacts = new UserContacts();
  countries: any;
  selectedCountryCode;
  selectedCountryPrefix: any;

  @ViewChild('instance')
  instance: NgbTypeahead;

  @ViewChild('elem')
  elem: ElementRef;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

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
              private router: Router) {
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnInit() {
    this.loadLookups();

    this.userService.find(this.authenticationService.currentUser?.id).subscribe(data => {
      if (data && data.id) {
        this.contactsForm.disable();
        this.contactsForm.controls['email'].setValue(data.email);
        this.contactsForm.controls['mobileNumber'].setValue(data.mobileNumber);
        this.originalMobileNo = data.mobileNumber
        this.originalEmail = data.email;
        this.originalCountryCode = data.countryCode.toLowerCase();
        this.selectedCountryCode = data.countryCode.toLowerCase();
        this.selectedCountryPrefix = '+' + data.countryPhonePrefix;
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));

      }
    });

    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });

    this.selectedSeason = JSON.parse((localStorage.getItem('selectedSeason')));
    this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    this.getRitualSeason();
    this.createForm();
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  updateUserLanguage() {
    this.enableEditLanguage = true;
    if (this.selectedLanguage != "" && this.selectedLanguage != this.currentLanguage) {
      this.setLanguage(this.selectedLanguage);
      this.userService.updatePreferredLang(this.selectedLanguage?.startsWith('ar') ? "ar" : "en").subscribe(response => {
        if (response && response.errors) {
          this.toastr.warning(this.translate.instant("general.dialog_error_text"), this.translate.instant("general.dialog_edit_title"));

        } else {
          this.toastr.success(this.translate.instant("general.dialog_edit_success_text"), this.translate.instant("general.dialog_edit_title"));

        }

      });
    }
  }

  private createForm() {

    this.contactsForm = this.formBuilder.group({
      mobileNumber: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [DccValidators.email, Validators.required]]
    });

  }

  getRitualSeason() {
    this.userService.getApplicantSeason().subscribe(seasons => {
      console.log('seasons', seasons);
      this.seasons = seasons;
      if (this.seasons.length > 0) {
        console.log("this.selectedSeason", this.selectedSeason)
        if (this.selectedSeason == null) {
          this.selectedSeason = this.seasons[0];
        }

        localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
        this.getApplicantRitualLiteBySeason(true);
      }
    })
  }

  getApplicantRitualLiteBySeason(firstCall: boolean) {

    if (this.selectedSeason) {
      this.applicantRituals = [];
      this.userService.getApplicantRitualLiteBySeason(this.selectedSeason).subscribe(applicantRituals => {
        this.applicantRituals = applicantRituals;
        if (this.applicantRituals.length > 0) {

          if (firstCall == true && this.selectedApplicantRitual != null) {
            let curSelected = this.applicantRituals.filter(ar => ar.id === this.selectedApplicantRitual.id);
            if (curSelected.length > 0) {
              this.changeSelectedApplicantRitual(this.selectedApplicantRitual);
            } else {
              this.changeSelectedApplicantRitual(this.applicantRituals[0]);
            }
          } else {
            this.changeSelectedApplicantRitual(this.applicantRituals[0]);
          }
        }
      });
    }
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  loadLookups() {
    this.cardService.findCountries().subscribe(result => {
      result.forEach(c => c.countryPhonePrefix = '+' + c.countryPhonePrefix);
      this.countries = result;
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.contactsForm?.controls;
  }

  onSeasonChange(event) {
    this.selectedSeason = event.target.value;
    this.getApplicantRitualLiteBySeason(false);
  }

  changeSelectedApplicantRitual(applicantRitual: ApplicantRitualLite) {
    this.selectedApplicantRitual = applicantRitual;
  }


  onApplicantRitualChange(event) {
    let curSelected = this.applicantRituals.filter(ar => ar.id == event.target.value)[0];
    this.changeSelectedApplicantRitual(curSelected);
  }

  saveSelectedRitual() {
    localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
    localStorage.setItem('selectedApplicantRitual', JSON.stringify(this.selectedApplicantRitual));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitual);

    this.enableEditRitual = false;
  }

  editRitual() {
    this.enableEditRitual = true;
  }

  setContactsEnabled() {
    this.contactsForm.enable();
  }

  onSubmit() {
    // trigger all validations
    Object.keys(this.contactsForm.controls).forEach(field => {
      const control = this.contactsForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    // stop here if form is invalid
    if (this.contactsForm.invalid) {

      console.log(this.contactsForm.controls['mobileNumber'].errors);
      return;
    }
    console.log(this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.replace(/\s/g, ""))
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

      console.log(this.userContacts);

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

    } else {
      this.contactsForm.disable();
    }
  }

  ngAfterViewInit() {
    document.querySelector('body').classList.add('settings');
  }

  ngOnDestroy() {
    document.querySelector('body').classList.remove('settings');
  }


  public openTypeahead(): void {
    // Dispatch event on input element that NgbTypeahead is bound to
    this.elem.nativeElement.dispatchEvent(new Event('input'));
    // Ensure input has focus so the user can start typing
    this.elem.nativeElement.focus();
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
    input.value = '';
  }

}
