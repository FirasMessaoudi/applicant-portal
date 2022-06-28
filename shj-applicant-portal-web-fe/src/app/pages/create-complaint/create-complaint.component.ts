import {ChangeDetectorRef, Component, Inject, OnInit, Renderer2} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {combineLatest} from 'rxjs';
import {map, take} from 'rxjs/operators';
import {TranslateService} from '@ngx-translate/core';
import {ToastService} from '@shared/components/toast';
import {I18nService} from '@dcc-commons-ng/services';
import {Lookup} from '@model/lookup.model';
import {LookupService} from '@core/utilities/lookup.service';
import {Marker} from '@model/marker.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConfirmDialogService} from '@shared/components/confirm-dialog';
import {ApplicantComplaint} from "@model/applicant-complaint.model";
import {ComplaintService} from "@core/services/complaint/complaint.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-complaint-details',
  templateUrl: './create-complaint.component.html',
  styleUrls: ['./create-complaint.component.scss'],
})
export class CreateComplaintComponent implements OnInit {
  complaint: ApplicantComplaint;
  complaintId: number;
  isLoading: boolean;
  isLocationSelected = false;
  complaintTypes: Lookup[] = [];
  complaintStatuses: Lookup[] = [];
  cities: Lookup[] = [];
  mapIsReady = false;
  MAP_ZOOM_OUT = 10;
  mapOptions: google.maps.MapOptions = {
    center: {lat: 21.423461874376475, lng: 39.825553299746616},
    zoom: this.MAP_ZOOM_OUT,
    disableDefaultUI: true,
  };
  marker: Marker;
  MARK_AS_RESOLVED: string = 'MARK_AS_RESOLVED';
  MARK_AS_CLOSED: string = 'MARK_AS_CLOSED';
  UNDER_PROCESSING: string = 'UNDER_PROCESSING';
  RESOLVED: string = 'RESOLVED';
  CLOSED: string = 'CLOSED';
  complaintForm: FormGroup;
  icon: any;
  url: any = 'assets/images/default-avatar.svg';
  file: File;
  progress = 0;

  constructor(
    private complaintService: ComplaintService,
    private router: Router,
    private i18nService: I18nService,
    private translate: TranslateService,
    private toastr: ToastService,
    private lookupsService: LookupService,
    private route: ActivatedRoute,
    private confirmDialogService: ConfirmDialogService,
    private formBuilder: FormBuilder,
    @Inject(DOCUMENT) private document: Document,
    private renderer2: Renderer2,
    private cdr: ChangeDetectorRef,

) {
  }

  ngOnInit(): void {

    this.initForm();
    this.loadLookups();
  }

  loadLookups() {
    this.complaintService.findComplaintTypes()
      .pipe(take(1))
      .subscribe((result) => {
      this.complaintTypes = result;
    });
    this.complaintService.findComplaintStatuses()
      .pipe(take(1))
      .subscribe((result) => {
      this.complaintStatuses = result;
    });
    this.lookupsService.findCities()
      .pipe(take(1))
      .subscribe((result) => {
        this.cities = result;
      });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  navigateToList() {
    this.router.navigate(['/complaints/list']);
  }

  get canSeeAddUpdateComplaints(): boolean {
    //TODO Update authorities
    // return this.authenticationService.hasAuthority(EAuthority.ADD_USER) || this.authenticationService.hasAuthority(EAuthority.EDIT_USER);
    return true;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  /**
   * Returns the css class for the given status
   *
   * @param status the current applicant complaint status
   */
  buildStatusClass(status: any): string {
    switch (status) {
      case 'UNDER_PROCESSING':
        return 'ready';
      case 'RESOLVED':
        return 'done';
      case 'CLOSED':
        return 'warning';
      default:
        return 'new';
    }
  }

  async loadGoogleMapsApiKey() {

  }

  private loadScript(key) {
    return new Promise((resolve, reject) => {
      const script = this.renderer2.createElement('script');
      script.type = 'text/javascript';
      script.src = 'https://maps.googleapis.com/maps/api/js?key=' + key;
      script.text = ``;
      script.async = true;
      script.defer = true;
      script.onload = resolve;
      script.onerror = reject;
      this.renderer2.appendChild(this.document.body, script);
    });
  }

  cancel() {
    this.confirmDialogService
      .confirm(
        this.translate.instant(
          'notification-management.cancel_confirmation_text'
        ),
        this.translate.instant('general.dialog_confirmation_title')
      )
      .then((confirm) => {
        if (confirm) {
          this.goBackToList();
        }
      });
  }

  goBackToList() {
    this.router.navigate(['/complaints/list']);
  }

  get f() {
    return this.complaintForm.controls;
  }

  save() {
    Object.keys(this.complaintForm.controls).forEach((field) => {
      const control = this.complaintForm.get(field);
      control.markAsTouched({onlySelf: true});
    });

    if (this.complaintForm.invalid) {
      return;
    }

    let confirmationText, successText;

    confirmationText ='complaint-management.dialog_create_complaint_confirmation_text';
    successText = 'complaint-management.dialog_create_complaint_success_text';

    this.confirmDialogService
      .confirm(
        this.translate.instant(confirmationText),
        this.translate.instant('general.dialog_confirmation_title')
      )
      .then((confirm) => {
        let payload = this.complaintForm.value;
        let complaint = new ApplicantComplaint;
        complaint.typeCode = payload.typeCode;
        complaint.city = payload.city;
        complaint.campNumber = payload.campNumber;
        complaint.description = payload.description;
        if (confirm) {
          this.isLoading = true;
          this.complaintService.createNewComplaint(complaint, this.file).subscribe(
            event => {
              if (event.type === HttpEventType.UploadProgress) {
                this.progress = Math.round(100 * event.loaded / event.total);
              } else if (event instanceof HttpResponse) {
                this.isLoading = false;
                this.toastr.success(
                  this.translate.instant(successText),
                  this.translate.instant(
                    'complaint-management.complaint_creation'
                  )
                );
                this.navigateToList();
              }
            },
            (error) => {
              this.progress = 0;
              this.isLoading = false;
              this.toastr.error(
                this.translate.instant('general.dialog_error_text'),
                this.translate.instant(
                  'complaint-management.complaint_creation'
                )
              );
            }
          );
        }
      });
  }

  private initForm() {
    this.complaintForm = this.formBuilder.group({
      typeCode: ['',Validators.required],
      description: ['', [Validators.required, Validators.maxLength(500)]],
      city: ['', [Validators.required]],
      campNumber: [''],
    });
  }

  isUnderProcessing(complaint): boolean {
    return complaint?.statusCode === this.UNDER_PROCESSING;
  }

  // downloadAttachment(id) {
  //   this.complaintService.downloadComplaintAttachment(id).subscribe(
  //     (data) => {
  //       this.downloadFile(data);
  //       console.log(data);
  //     },
  //     (error) => console.log('Error downloading the file.'),
  //     () => console.info('OK')
  //   );
  // }

  downloadFile(data) {
    const blob = new Blob([data], {type: 'image/jpg'});
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  }

  onFileChange(event) {
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]); // read file as data url
      reader.onload = () => { // called once readAsDataURL is completed
        const fileSizeInMb = Math.round(event.target.files[0].size / 1024 / 1024);
        if (fileSizeInMb < 0 || fileSizeInMb > 15) {
          this.toastr.error(this.translate.instant('staff-management.file_size_invalid'), this.translate.instant('staff-management.change-avatar'));
          return;
        }
        this.url = reader.result.toString();
        this.file = event.target.files[0];
      };
    }
  }

  onCityChange(val: any){
    if (val == 'HOLY_SITES'){
      this.complaintForm.get('campNumber').setValidators([Validators.required,Validators.maxLength(40)])
      this.complaintForm.get('campNumber').markAsUntouched()
      this.complaintForm.get('campNumber').updateValueAndValidity();
    } else {
      this.complaintForm.get('campNumber').setValidators(null)
      this.complaintForm.get('campNumber').markAsUntouched()
      this.complaintForm.get('campNumber').updateValueAndValidity();
    }
  }
}
