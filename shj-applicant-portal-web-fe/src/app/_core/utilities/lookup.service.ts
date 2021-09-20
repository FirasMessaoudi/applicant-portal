import {I18nService} from "@dcc-commons-ng/services";
import {Injectable} from "@angular/core";
import {Lookup} from "@model/lookup.model";

const defaultLang = 'ar';

@Injectable({
  providedIn: "root"
})
export class LookupService {

  constructor(private i18nService: I18nService) {
  }

  localizedLabel(lookupItems: Lookup[], code: string): string {
    let item: Lookup = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    return item?.label;
  }

  localizedDescription(lookupItems: any[], code: string): string {
    let item = lookupItems.find(type => type.code === code && this.i18nService.language.startsWith(type.lang));
    if (!item)
      item = lookupItems.find(type => type.code === code && type.lang.startsWith(defaultLang));
    console.log(item);
    return item?.description;
  }
}
