import {ApplicantPackageCatering} from "@model/applicant-package-catering.model";
import {ApplicantPackageHousing} from "@model/applicant-package-housing.model";
import {ApplicantPackageTransportation} from "@model/applicant-package-transportation.model";
import {CompanyLite} from "@model/company-lite.model";


export class ApplicantPackageDetails {
  applicantPackageCaterings: ApplicantPackageCatering [] = [];
  applicantPackageHousings: ApplicantPackageHousing [] = [];
  applicantPackageTransportations: ApplicantPackageTransportation [] = [];
  companyLite: CompanyLite;

}
