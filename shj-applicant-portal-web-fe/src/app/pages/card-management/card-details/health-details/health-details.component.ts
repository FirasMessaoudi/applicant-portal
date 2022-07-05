import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ApplicantHealth} from "@model/applicant-health.model";
import {LookupService} from "@core/utilities/lookup.service";
import {Lookup} from "@model/lookup.model";
import {ModalDialogConfig} from "@model/modal-dialog-models/modal-dialog-config";
import {TranslateService} from "@ngx-translate/core";
import {ModalDialogComponent} from "@shared/components/modal-dialog/modal-dialog.component";
import {HealthProfileComponent} from "@pages/card-management/card-details/health-profile/health-profile.component";
import * as cloneDeep from 'lodash/cloneDeep';
import {take} from "rxjs/operators";
import {CardService} from "@core/services";

@Component({
  selector: 'app-health-details',
  templateUrl: './health-details.component.html',
  styleUrls: ['./health-details.component.scss']
})
export class HealthDetailsComponent implements OnInit {
  @Input()
  healthDetails: ApplicantHealth;
  @Input()
  applicantId: number;
  @Input()
  healthSpecialNeeds: Lookup[];
  @Input()
  immunizations: Lookup[];
  @Input()
  currentLanguage: string;
  updateHealthProfileModalConfig: ModalDialogConfig;
  @ViewChild('updateHealthProfileModal') private updateHealthProfileModal: ModalDialogComponent;
  @ViewChild('healthProfile') private healthProfile: HealthProfileComponent;

  constructor(
    private lookupsService: LookupService,
    private translate: TranslateService,
    private cardService: CardService,


  ) {
  }

  ngOnInit(): void {
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  async openUpdateHealthProfileModal() {
    this.updateHealthProfileModalConfig = {
      modalTitle: this.translate.instant('general.update_health_profile'),
      closeButtonLabel: this.translate.instant('general.btn_save'),
      dismissButtonLabel: this.translate.instant('general.btn_cancel')
    };
    //this.loadHealthDetails(true);
    return await this.updateHealthProfileModal.open({
      size: 'xl',
      backdrop: 'static',
      keyboard: false
    });
  }

  updateHealthProfile() {
    this.healthDetails = cloneDeep(this.healthProfile.updatedHealthProfile);
    console.log(this.healthDetails);
    this.healthDetails.applicant = {'id': this.applicantId};
    if (this.healthDetails.diseases) {
      Object.keys(this.healthDetails.diseases).forEach(key => {
        if (this.healthDetails.diseases[key].newId) {
          delete this.healthDetails.diseases[key].newId;
          this.healthDetails.diseases[key].id = 0;
        }
      });
    }
    this.cardService.updateHealthProfile(this.healthDetails)
      .pipe(take(1))
      .subscribe(result => {
       /* this.toastr.success(
          this.translate.instant('applicant-management.health_profile_updated_successfully'),
          this.translate.instant('general.dialog_edit_title')
        );*/
      }, () => {
       /* this.toastr.error(
          this.translate.instant('general.dialog_error_text'),
          this.translate.instant('general.dialog_error_title')
        );*/
      });
  }

  cancelUpdateHealthProfile() {
    this.healthProfile.resetHealthProfile();
    //this.loadHealthDetails(true);
  }

}
