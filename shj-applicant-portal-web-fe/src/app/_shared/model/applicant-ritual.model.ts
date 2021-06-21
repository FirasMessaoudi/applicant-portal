import {Applicant} from "@model/applicant.model";

export class ApplicantRitual {
  id: number;
  applicant: Applicant;
  hamlahPackageCode: string;
  hijriSeason: number;
  dateStartGregorian: Date;
  dateEndGregorian: Date;
  dateStartHijri: Date;
  dateEndHijri: Date;
  typeCode: any;
  visaNumber: string;
  permitNumber: string;
  insuranceNumber: string;
  borderNumber: string;
}
