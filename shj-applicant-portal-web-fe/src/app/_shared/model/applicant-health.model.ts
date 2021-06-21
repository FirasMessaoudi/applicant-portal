import {ApplicantHealthDisease} from "@model/applicant-health-disease.model";
import {ApplicantHealthSpecialNeeds} from "@model/applicant-health-special-needs.model";
import {ApplicantHealthImmunization} from "@model/applicant-health-immunization.model";
import {Applicant} from "@model/applicant.model";

export class ApplicantHealth {
  id: number
  applicant: Applicant
  bloodType: string;
  creationDate: Date;
  diseases: ApplicantHealthDisease[];
  specialNeeds: ApplicantHealthSpecialNeeds[];
  immunizations: ApplicantHealthImmunization[];
}
