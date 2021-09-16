import {PackageCatering} from "@model/package-catering.model";
import {HousingZone} from "@model/housing-zone.model";
import {RitualPackage} from "@model/ritual-package.model";

export class PackageHousing {
    id: number;
    type: string;
    typeCode: string;
    locationNameAr: string;
    locationNameEn: string;
    referenceNumber: string;
    validityStart: Date;
    validityEnd: Date;
    addressAr: string;
    addressEn: string;
    default: boolean;
    lat: String;
    lng: string;

    packageCaterings: PackageCatering[];
    housingZone: HousingZone;
    categoryCode: string;
    ritualPackage: RitualPackage;
}
