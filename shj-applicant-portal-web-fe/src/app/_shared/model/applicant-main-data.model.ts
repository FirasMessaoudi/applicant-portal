import {ApplicantRelative} from "@model/applicant-relative.model";
import {ApplicantContact} from "@model/applicant-contact.model";
import {ApplicantHealth} from "@model/applicant-health.model";
import {ApplicantDigitalId} from "@model/applicant-digital-id.model";
import {Applicant} from "@model/applicant.model";

export class ApplicantMainData extends Applicant{

  uin: string;
  ritualTypeCode: string;
  cardReferenceNumber: string;
  cardStatusCode: any;

}
