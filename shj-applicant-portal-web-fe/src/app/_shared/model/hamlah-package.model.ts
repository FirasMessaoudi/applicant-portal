import {Lookup} from "@model/lookup.model";
import {PackageHousing} from "@model/package-housing.model";
import {PackageTransportation} from "@model/package-transportation.model";

export class HamlahPackage {
  id: number;
  typeCode: Lookup;
  hamlahId: number;
  campId: number;
  price: number;
  departureCity: string;
  countryId: number;
  packageHousings: PackageHousing[];
  packageTransportations: PackageTransportation[];
}
