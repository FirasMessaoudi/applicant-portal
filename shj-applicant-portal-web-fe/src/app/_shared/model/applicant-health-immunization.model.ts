import {ApplicantHealth} from "@model/applicant-health.model";

export class ApplicantHealthImmunization {
  id: number;
  applicantHealth: ApplicantHealth;
  immunizationCode: string;
  immunizationDate: Date;
  mandatory: boolean;
  creationDate: Date;
}
