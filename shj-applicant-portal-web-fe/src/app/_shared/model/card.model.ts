import {ApplicantRitual} from "@model/applicant-ritual.model";
import {ApplicantPackageTransportation} from "@model/applicant-package-transportation.model";

export class Card {
  id: number;
  applicantRitual: ApplicantRitual;
  referenceNumber: number;
  batchId: number;
  statusCode: any;
  applicantPackageTransportations: ApplicantPackageTransportation [] = [];
}
