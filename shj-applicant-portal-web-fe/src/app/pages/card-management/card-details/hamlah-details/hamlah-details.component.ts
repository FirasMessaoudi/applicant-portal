import {Component, Input, OnInit} from '@angular/core';
import {ApplicantPackageDetails} from "@model/applicant-package-details.model";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";

@Component({
  selector: 'app-hamlah-details',
  templateUrl: './hamlah-details.component.html',
  styleUrls: ['./hamlah-details.component.scss']
})
export class HamlahDetailsComponent implements OnInit {

  @Input()
  applicantPackage: ApplicantPackageDetails =null;

  @Input()
  housingCategories: Lookup[];
  @Input()
  housingTypes: Lookup[];
  @Input()
  packageTypes: Lookup[];
  @Input()
  housingSites: Lookup[];

  @Input()
  currentLanguage: string;

  @Input()
  transportationTypes: Lookup[];

  constructor(private lookupsService: LookupService) {
  }

  ngOnInit(): void {
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }


  removeSeconds(mealTime: any) {
   var result= mealTime.split(":",2)
    return result[0]+":"+result[1];
  }

  removeSecondsAddAmPm(mealTime: any) {
    var result= mealTime.split(":",2)
    var timeAmPm =  result[0] >= 12 ? 'PM' : 'AM';
    result[0] = result[0] % 12;
    result[0] = result[0] ? result[0] : 12; // the hour '0' should be '12'
    var strTime = result[0] + ':' + result[1] + ' ' + timeAmPm;
    return strTime;
  }

  getTransportationIcon(transportationType:string){
    if(transportationType == "CAR"){
      return "car-solid";
    } else if(transportationType == "BUS"){
      return "bus-solid";
    } else if(transportationType == "TRAIN"){
      return "train-solid";
    } else if(transportationType == "AIRPLANE"){
      return "plane-departure-solid";
    } else {
      return "";
    }
  }
}
