import {PackageHousing} from "@model/package-housing.model";

export class PackageCatering {
    id: number;
    mealCode: string;
    mealTime: number;
    mealDescription: string;
    type: string;
    descriptionAr: string;
    descriptionEn: string;
    packageHousing: PackageHousing;
}
