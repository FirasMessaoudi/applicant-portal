import {ApplicantIncident} from "@model/applicant-incident.model";

export class IncidentAttachment {
  id: number;
  filePath: string;
  applicantIncident: ApplicantIncident;
  creationDate: Date;
}
