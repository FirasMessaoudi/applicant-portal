import {PackageHousing} from "@model/package-housing.model";

export class PackageCatering {
    id: number;
    mealTimeCode: number;
    mealTypeCode: string;
    descriptionAr: string;
    descriptionEn: string;
    packageHousing: PackageHousing;
}
