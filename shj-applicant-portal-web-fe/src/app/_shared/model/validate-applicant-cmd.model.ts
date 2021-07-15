import {Applicant} from "@model/applicant.model";

export class ValidateApplicantCmd {
  uin: any;
  dateOfBirthGregorian: any;
  dateOfBirthHijri: any;

  constructor(uin: any, dateOfBirthGregorian:any,dateOfBirthHijri:any){
    this.uin = uin;
    this.dateOfBirthGregorian = dateOfBirthGregorian;
    this.dateOfBirthHijri = dateOfBirthHijri;
  }

}
