import {ApplicantHealth} from "@model/applicant-health.model";

export class ApplicantHealthSpecialNeeds {
  id: number;
  applicantHealth: ApplicantHealth;
  specialNeedTypeCode: string;
  creationDate: Date;
}
