import {ApplicantHealth} from "@model/applicant-health.model";

export class ApplicantHealthDisease {
  id: number;
  applicantHealth: ApplicantHealth;
  diseaseNameAr: string;
  diseaseNameEn: string;
  creationDate: Date;
}
