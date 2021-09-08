import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AuthenticationService, CardService, UserService} from "@core/services";
import {LookupService} from "@core/utilities/lookup.service";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DccValidators} from "@shared/validators";
import {UserContacts} from "@model/UserContacts.model";
import {Router} from "@angular/router";
import {User} from "@shared/model";
import {CountryISO} from 'ngx-intl-tel-input';
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, AfterViewInit, OnDestroy {
  closeResult = '';


  enableEditLanguage = true;
  selectedLanguage = "";
  contactsForm: FormGroup;
  originalEmail: any;
  originalMobileNo: any;
  originalCountryCode: any;
  userContacts: UserContacts = new UserContacts();
  selectedCountryCode = "SA";
  formattedCountryDial: any;

  constructor(private modalService: NgbModal,
              private userService: UserService,
              private cardService: CardService,
              private i18nService: I18nService,
              private toastr: ToastService,
              private formBuilder: FormBuilder,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private lookupsService: LookupService,
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
    this.userService.find(this.authenticationService.currentUser?.id).subscribe(data => {
      if (data && data.id) {

        this.contactsForm.disable();
        this.contactsForm.controls['email'].setValue(data.email);
        this.contactsForm.controls['mobileNumber'].setValue(data.mobileNumber);
        this.originalMobileNo = data.mobileNumber
        this.originalEmail = data.email;
        this.originalCountryCode = data.countryCode.toLowerCase();
        this.selectedCountryCode = data.countryCode.toLowerCase();
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.authenticationService.currentUser.id}),
          this.translate.instant('general.dialog_error_title'));

      }
    });


    this.createForm();
  }

  public onFocus(e: Event): void {
    e.stopPropagation();
    setTimeout(() => {
      const inputEvent: Event = new Event('input');
      e.target.dispatchEvent(inputEvent);
    }, 0);
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
      mobileNumber: ['', Validators.required],
      email: ['', [DccValidators.email, Validators.required]]
    });

  }


  lookupService(): LookupService {
    return this.lookupsService;
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.contactsForm?.controls;
  }


  setContactsEnabled() {
    this.contactsForm.enable();

  }

  getPreferredCountries(): Array<any> {
    const preferredCountries = [CountryISO.SaudiArabia, this.originalCountryCode];
    const uniqueCountrySet = new Set(preferredCountries);
    return [...uniqueCountrySet];
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
    console.log(this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.number.replace(/\s/g, ""))
    if (this.originalMobileNo != this.contactsForm.controls['mobileNumber'].value.number.replace(/\s/g, "") ||
      this.originalEmail != this.contactsForm.controls['email'].value ||
      this.originalCountryCode.toUpperCase() != this.contactsForm.controls['mobileNumber'].value.countryCode.toUpperCase()) {


      let reg1 = / /g;
      let reg2 = /\+/gi;
      let reg3 = /\-/gi;

      this.userContacts.countryCode = this.contactsForm.controls['mobileNumber'].value.countryCode.toUpperCase();
      this.userContacts.email = this.contactsForm.controls['email'].value;
      this.userContacts.mobileNumber = this.contactsForm.controls['mobileNumber'].value.number.replace(reg1, "").replace(reg3, "");
      this.formattedCountryDial = this.contactsForm.controls['mobileNumber'].value.dialCode.replace(reg2, "");
      this.userContacts.countryPhonePrefix = this.formattedCountryDial;

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

}
