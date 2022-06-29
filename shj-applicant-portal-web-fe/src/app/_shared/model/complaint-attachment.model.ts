import {ApplicantIncident} from "@model/applicant-incident.model";

export class ComplaintAttachment {
  id: number;
  filePath: string;
  applicantIncident: ApplicantIncident;
  creationDate: Date;
}
