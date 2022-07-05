import {I18nService} from "@dcc-commons-ng/services";
import {Injectable} from "@angular/core";
import {Lookup} from "@model/lookup.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CountryLookup} from "@model/country-lookup.model";


const defaultLang = 'ar';

@Injectable({
  providedIn: "root"
})
export class LookupService {

  constructor(private i18nService: I18nService,
              private http: HttpClient) {
  }

  localizedLabel(lookupItems: Lookup[], code: string): string {
    let item: Lookup = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.label;
  }

  // localizedLabelByCountryPhonePrefix(lookupItems: CountryLookup[], countryPhonePrefix: string): string {
  //   let item: Lookup = lookupItems.find(type => type.countryPhonePrefix === countryPhonePrefix && this.i18nService.language.startsWith(type.lang));
  //   if (!item)
  //     item = lookupItems.find(type => type.code === countryPhonePrefix && type.lang.startsWith(defaultLang));
  //   return item?.label;
  // }

  localizedDescription(lookupItems: Lookup[], code: string): string {
    let item: Lookup = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.description;
  }

  localizedSummary(lookupItems: Lookup[], code: string): string {
    let item: Lookup = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.summary;
  }

  localizedNotificationDescription(lookupItems: Lookup[], code: string): string {
    let item: any = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.sample;
  }

  localizedNotificationMandatory(lookupItems: Lookup[], code: string): boolean {
    let item: any = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.mandatory;
  }

  loadGoogleMapKey(): Observable<any> {
    return this.http.get<any>('/core/api/lookup/map-key', {responseType: 'text' as 'json'});
  }


  localizedItemsByLang(lookupItems: any[]): any[] {
    return lookupItems.filter(value => this.i18nService.language.startsWith(value.lang));
  }

  findCities(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/city/list');
  }
}
