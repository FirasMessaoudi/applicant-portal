import {Injectable} from '@angular/core';
import {I18nService} from '@dcc-commons-ng/services';
import {TranslateService} from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class CustomI18nService extends I18nService {

  constructor(translateService: TranslateService) {
    super(translateService)
  }

  init(defaultLanguage: string, supportedLanguages: string[]) {
    super.init(defaultLanguage, supportedLanguages);
  }

  get language(){
    return super.language;
  }

  set language(language: string){
    super.language = language;
    if (language && (language.startsWith('ar') || language.startsWith('fa'))) {
      document.querySelector("html").setAttribute('dir', 'rtl');
      document.querySelector("html").setAttribute('lang', language);
    } else {
      document.querySelector("html").setAttribute('dir', 'ltr');
      document.querySelector("html").setAttribute('lang', language);
    }
  }
}
