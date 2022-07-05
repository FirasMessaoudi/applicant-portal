import {ApplicantHealthDisease} from "@model/applicant-health-disease.model";
import {ApplicantHealthSpecialNeeds} from "@model/applicant-health-special-needs.model";
import {ApplicantHealthImmunization} from "@model/applicant-health-immunization.model";

export class ApplicantHealth {
  id: number
  applicant: any
  bloodType: string;
  creationDate: Date;
  diseases: ApplicantHealthDisease[];
  specialNeeds: ApplicantHealthSpecialNeeds[];
  immunizations: ApplicantHealthImmunization[];
}
