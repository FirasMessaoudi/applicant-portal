import {Component, Inject, OnInit, Renderer2} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {combineLatest} from 'rxjs';
import {map, take} from 'rxjs/operators';
import {ApplicantComplaint} from '@model/applicant-complaint.model';
import {ComplaintService} from '@core/services/complaint/complaint.service';
import {TranslateService} from '@ngx-translate/core';
import {ToastService} from '@shared/components/toast';
import {I18nService} from '@dcc-commons-ng/services';
import {Lookup} from '@model/lookup.model';
import {LookupService} from '@core/utilities/lookup.service';
import {Marker} from '@model/marker.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConfirmDialogService} from '@shared/components/confirm-dialog';

@Component({
  selector: 'app-complaint-details',
  templateUrl: './complaint-details.component.html',
  styleUrls: ['./complaint-details.component.scss'],
})
export class ComplaintDetailsComponent implements OnInit {
  complaint: ApplicantComplaint;
  complaintId: number;
  isLoading: boolean;
  complaintTypes: Lookup[] = [];
  complaintStatuses: Lookup[] = [];
  cities: Lookup[] = [];
  marker: Marker;
  icon: any;

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
    private renderer2: Renderer2
  ) {
  }

  ngOnInit(): void {

    combineLatest([this.route.params, this.route.queryParams])
      .pipe(
        map((results) => ({
          params: results[0].id,
          qParams: results[1],
        }))
      )
      .subscribe((results) => {
        this.complaintId = +results.params; // (+) converts string 'id' to a number
        if (this.complaintId) {
          this.isLoading = true;
          // load complaint details
          this.complaintService.find(this.complaintId).subscribe((data) => {
            if (data && data.id) {
              this.isLoading = false;
              this.complaint = data;
            } else {
              this.toastr.error(
                this.translate.instant('general.route_item_not_found', {
                  itemId: this.complaintId,
                }),
                this.translate.instant('general.dialog_error_title')
              );
              this.navigateToList();
            }
          });
        } else {
          this.toastr.error(
            this.translate.instant('general.route_id_param_not_found'),
            this.translate.instant('general.dialog_error_title')
          );
          this.navigateToList();
        }
      });
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
    this.router.navigate(['/complaint/list']);
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

  downloadAttachment(id, fileName) {
    this.complaintService.downloadComplaintAttachment(id).subscribe(
      (data) => {
        this.downloadFile(data, fileName);
        console.log(data);
      },
      (error) => console.log('Error downloading the file.'),
      () => console.info('OK')
    );
  }

  downloadFile(data, fileName) {
    const blob = new Blob([data], {type: this.getMimeByExt(fileName)});
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  }

  getMimeByExt(fileName) {
    var ext = this.getFileExt(fileName);
    if (this.extToMimes.hasOwnProperty(ext)) {
      return this.extToMimes[ext];
    }
    return false;
  }

  getFileExt(sFileName) {
    for (var j = 0; j < this.allowedFileExtension.length; j++) {
      var sCurExtension = this.allowedFileExtension[j];
      if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
        return sCurExtension;
      }
    }
  }

  allowedFileExtension = ['apng','avif','gif','jpeg','jpg','png','svg','webp','bmp','tiff','mp4','mov','wmv','avi','flv','avchd','mkv'];

  extToMimes = {
    'apng': 'image/png',
    'avif': 'image/avif',
    'gif': 'image/gif',
    'jpeg': 'image/jpeg',
    'jpg': 'image/jpeg',
    'png': 'image/png',
    'svg': 'image/svg+xml',
    'webp': 'image/webp',
    'bmp': 'image/bmp',
    'tiff': 'image/tiff',
    'mp4': 'video/mp4',
    'mov': 'video/quicktime',
    'wmv': 'video/x-ms-wmv',
    'avi': 'video/avi',
    'flv': 'video/x-flv',
    'avchd': 'video/avchd-stream',
    'mkv': 'video/x-matroska',
  }
}
