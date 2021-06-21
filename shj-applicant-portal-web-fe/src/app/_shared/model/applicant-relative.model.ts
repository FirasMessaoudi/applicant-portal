import {Applicant} from "@model/applicant.model";

export class ApplicantRelative {
  id: number;
  relationshipCode: any;
  relativeApplicant: Applicant;
  applicant: Applicant;
}
