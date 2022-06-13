import {Lookup} from "@model/lookup.model";

export class CountryLookup extends Lookup {
  nicCode: string;
  countryPhonePrefix: string;
  countryNamePrefix;
}
