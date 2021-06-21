import {PackageCatering} from "@model/package-catering.model";

export class PackageHousing {
  id: number;
  type: string;
  code: string;
  labelAr: string;
  labelEn: string;
  validityStart: any;
  validityEnd: any;
  addressAr: string;
  addressEn: string;
  isDefault: boolean;
  packageCaterings: PackageCatering[];
}
