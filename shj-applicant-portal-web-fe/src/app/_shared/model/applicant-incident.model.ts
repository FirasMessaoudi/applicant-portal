import {ApplicantRitual} from "@model/applicant-ritual.model";
// import {ComplaintAttachment} from "@model/complaint-attachment.model";

export class ApplicantIncident {
  id: number;
  applicantRitual: ApplicantRitual;
  statusCode: string;
  referenceNumber: string;
  typeCode: string;
  description: string;
  locationLat: number;
  locationLng: number;
  resolutionComment: string;
  // incidentAttachment: IncidentAttachment;
  creationDate: Date;
  updateDate: Date;
  areaCode: string;
  city: string;
  campNumber: string;
  crmTicketNumber: string;
}
