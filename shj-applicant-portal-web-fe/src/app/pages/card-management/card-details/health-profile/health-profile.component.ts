import {Component, Input, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {ApplicantHealth} from '@model/applicant-health.model';
import {Lookup} from '@model/lookup.model';
import {LookupService} from '@core/utilities/lookup.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import * as cloneDeep from 'lodash/cloneDeep';
import {ApplicantHealthSpecialNeeds} from '@model/applicant-health-special-needs.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {HijriGregorianDatepickerComponent} from '@shared/modules/hijri-gregorian-datepicker/datepicker/hijri-gregorian-datepicker.component';
import {ApplicantHealthImmunization} from '@model/applicant-health-immunization.model';
import {ApplicantHealthDisease} from '@model/applicant-health-disease.model';
import {DateFormatterService} from "@shared/modules/hijri-gregorian-datepicker/datepicker/date-formatter.service";
import {DateType} from "@shared/modules/hijri-gregorian-datepicker/datepicker/consts";

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'app-health-profile',
  templateUrl: './health-profile.component.html',
  styleUrls: ['./health-profile.component.scss']
})
export class HealthProfileComponent implements OnInit {
  @Input() healthDetails: ApplicantHealth;
  @Input() healthSpecialNeeds: Lookup[];
  @Input() immunizations: Lookup[];
  @Input() healthChronicDiseases;
  @Input() currentLanguage: string;

  @ViewChild('datePicker') datePicker: HijriGregorianDatepickerComponent;

  bloodTypes = [{"code": "A+"}, {"code": "A-"}, {"code": "B+"}, {"code": "B-"}, {"code": "O+"}, {"code": "O-"}, {"code": "AB+"}, {"code": "AB-"}];
  bloodTypeEditMode = false;
  specialNeedAddMode = false;
  diseaseAddMode = false;
  immunizationAddMode = false;
  bloodTypeEditForm: FormGroup;
  specialNeedAddForm: FormGroup;
  diseaseAddForm: FormGroup;
  immunizationAddForm: FormGroup;
  updatedHealthProfile: ApplicantHealth;
  maxVaccineDateGregorian: NgbDateStruct;
  maxVaccineDateHijri: NgbDateStruct;
  selectedDateType: any;
  vaccineDateString: any;
  isUpdated = false;

  constructor(
    private lookupsService: LookupService,
    private formBuilder: FormBuilder,
    private dateFormatterService: DateFormatterService,
  ) {
  }

  ngOnInit(): void {
    this.initCalendar();
    this.initForms();
  }

  private initCalendar() {
    const todayGregorian = this.dateFormatterService.todayGregorian();
    const todayHijri = this.dateFormatterService.todayHijri();
    this.maxVaccineDateGregorian = todayGregorian;
    this.maxVaccineDateHijri = todayHijri;
    this.selectedDateType = DateType.Gregorian;
  }

  onVaccineDateChange(event) {
    if (event) {
      this.immunizationAddForm.controls.immunizationDate.setValue(
        this.dateFormatterService.toDate(event)
      );
    }
  }

  private initForms() {
    this.bloodTypeEditForm = this.formBuilder.group({
      bloodType: [null]
    });
    this.specialNeedAddForm = this.formBuilder.group({
      specialNeedTypeCode: [null, Validators.required]
    })
    this.diseaseAddForm = this.formBuilder.group({
      disease: ['', [Validators.required, Validators.maxLength(30)]]
    })
    this.immunizationAddForm = this.formBuilder.group({
      immunizationCode: [null, Validators.required],
      immunizationDate: [null, Validators.required],
      mandatory: true
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.healthDetails) {
      this.updatedHealthProfile = cloneDeep(this.healthDetails);
      console.log(this.updatedHealthProfile);
    }
  }

  get fSpecialNeed() {
    return this.specialNeedAddForm.controls;
  }

  get fDisease() {
    return this.diseaseAddForm.controls;
  }

  get fImmune() {
    return this.immunizationAddForm.controls;
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  editBloodType() {
    this.bloodTypeEditForm.controls.bloodType.setValue(this.updatedHealthProfile.bloodType);
    this.bloodTypeEditMode = true;
  }

  saveBloodType() {
    this.bloodTypeEditMode = false;
    this.updatedHealthProfile.bloodType = this.bloodTypeEditForm.get('bloodType').value;
    this.isUpdated = true;
  }

  resetHealthProfile() {
    this.bloodTypeEditMode = false;
    this.specialNeedAddMode = false;
    this.diseaseAddMode = false;
    this.immunizationAddMode = false;

    this.specialNeedAddForm.reset();
    this.bloodTypeEditForm.reset();
    this.diseaseAddForm.reset();
    this.immunizationAddForm.reset();
  }

  addSpecialNeed() {
    if (this.allowedSpecialNeeds && this.allowedSpecialNeeds.length > 0) {
      this.specialNeedAddMode = true;
    }
  }

  addDisease() {
    this.diseaseAddMode = true;
  }

  addImmunization() {
    if (this.allowedVaccines && this.allowedVaccines.length > 0) {
      this.immunizationAddMode = true;
    }
  }

  cancelSpecialNeedAdd() {
    this.specialNeedAddForm.reset();
    this.specialNeedAddMode = false;
  }

  cancelDiseaseAdd() {
    this.diseaseAddForm.reset();
    this.diseaseAddMode = false;
  }

  cancelImmunizationAdd() {
    this.immunizationAddForm.reset();
    this.immunizationAddMode = false;
  }

  deleteSpecialNeed(specialNeedCodeDeleted: string) {
    this.updatedHealthProfile.specialNeeds = this.updatedHealthProfile.specialNeeds.filter(
      ({specialNeedTypeCode}) => specialNeedTypeCode !== specialNeedCodeDeleted);
  }

  deleteDisease(diseaseId: number) {
    this.updatedHealthProfile.diseases = this.updatedHealthProfile.diseases.filter(({id}) => id !== diseaseId);
  }

  deleteImmunization(immunizationCodeDeleted: string) {
    this.updatedHealthProfile.immunizations = this.updatedHealthProfile.immunizations.filter(
      ({immunizationCode}) => immunizationCode !== immunizationCodeDeleted);
  }

  saveSpecialNeed() {
    // trigger form validation
    this.specialNeedAddForm.markAllAsTouched();
    if (this.specialNeedAddForm.invalid) {
      return;
    }

    let newSpecialNeed = this.specialNeedAddForm.value;

    if (!this.updatedHealthProfile.specialNeeds) {
      this.updatedHealthProfile.specialNeeds = [];
    }
    this.updatedHealthProfile.specialNeeds.push(<ApplicantHealthSpecialNeeds>newSpecialNeed);

    // cancel add mode and reset form
    this.specialNeedAddMode = false;
    this.specialNeedAddForm.reset();
  }

  saveDisease() {
    // trigger form validation
    this.diseaseAddForm.markAllAsTouched();
    if (this.diseaseAddForm.invalid) {
      return;
    }

    // add new disease
    let addedDisease = this.diseaseAddForm.value;

    let newDisease = {
      labelAr: this.currentLanguage.startsWith('ar') ? addedDisease.disease : null,
      labelEn: !this.currentLanguage.startsWith('ar') ? addedDisease.disease : null
    };
    if (!this.updatedHealthProfile.diseases) {
      this.updatedHealthProfile.diseases = [];
    }
    newDisease['newId'] = this.updatedHealthProfile.diseases.length + 1000000;
    this.updatedHealthProfile.diseases.push(<ApplicantHealthDisease>newDisease);

    // cancel add mode and reset form
    this.diseaseAddMode = false;
    this.diseaseAddForm.reset();
  }

  saveImmunization() {
    // trigger form validation
    this.immunizationAddForm.markAllAsTouched();
    if (this.immunizationAddForm.invalid) {
      return;
    }

    // add new immunization
    // let newImmunization = {
    //   immunizationCode: this.immunizationAddForm.get('vaccine').value,
    //   immunizationDate: this.immunizationAddForm.get('vaccineDate').value
    // };
    let newImmunization = this.immunizationAddForm.value;
    if (!this.updatedHealthProfile.immunizations) {
      this.updatedHealthProfile.immunizations = [];
    }
    this.updatedHealthProfile.immunizations.push(<ApplicantHealthImmunization>newImmunization);

    // cancel add mode and reset form
    this.immunizationAddMode = false;
    this.immunizationAddForm.reset();
  }

  get allowedSpecialNeeds() {
    if (this.updatedHealthProfile?.specialNeeds && this.updatedHealthProfile?.specialNeeds.length > 0) {
      let allowedNeeds = this.healthSpecialNeeds.filter(newNeed =>
        !this.updatedHealthProfile.specialNeeds.find(existNeed =>
          existNeed.specialNeedTypeCode === newNeed.code))
        .filter(need => this.currentLanguage.startsWith(need.lang));
      return allowedNeeds;
    } else {
      return this.healthSpecialNeeds.filter(need => this.currentLanguage.startsWith(need.lang));
    }
  }

  get allowedDiseases() {
    let allowed = this.healthChronicDiseases.filter(newDisease =>
      !this.updatedHealthProfile.diseases.find(existDisease =>
        existDisease.id === newDisease.id));
    return allowed;
  }

  get allowedVaccines() {
    if (this.updatedHealthProfile.immunizations && this.updatedHealthProfile.immunizations.length > 0) {
      let allowedVaccines = this.immunizations.filter(newVaccine =>
        !this.updatedHealthProfile.immunizations.find(existVaccine =>
          existVaccine.immunizationCode === newVaccine.code))
        .filter(need => this.currentLanguage.startsWith(need.lang));
      return allowedVaccines;
    } else {
      return this.immunizations.filter(need => this.currentLanguage.startsWith(need.lang));
    }
  }

  cancelEditBloodType() {
    this.bloodTypeEditMode = false;
    this.bloodTypeEditForm.reset();
  }
}
