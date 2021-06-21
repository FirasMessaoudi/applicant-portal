import {AbstractControl, ValidatorFn, Validators} from '@angular/forms';

export enum IdType {
  NIN_ONLY = 1,
  IQAMA_ONLY = 2,
  NIN_OR_IQAMA = 3

}

export class DccValidators extends Validators {

  static arabicCharacters(lettersOnly: boolean): ValidatorFn {
    return (control: AbstractControl): { [key: string]: string } | null => {

      let regexpLettersOnly: RegExp = /^[\u0600-\u06EF]+$/g;
      let regexpLettersAndNumbers: RegExp = /^[\u0600-\u06FF, 0-9]+$/g;

      let regexp: RegExp = lettersOnly ? regexpLettersOnly: regexpLettersAndNumbers;

      if (control.value !== undefined && !regexp.test(control.value)) {
        return {'invalid-characters': 'dcc.commons.validation.constraints.arabic-characters'};
      }
      return null;
    }
  }

  static latinCharacters(lettersOnly: boolean): ValidatorFn {
    return (control: AbstractControl): { [key: string]: string } | null => {

      let regexpLettersOnly: RegExp = /^[A-Z, a-z]+$/g;
      let regexpLettersAndNumbers: RegExp = /^[A-Z, a-z, 0-9]+$/g;

      let regexp: RegExp = lettersOnly ? regexpLettersOnly: regexpLettersAndNumbers;

      if (control.value !== undefined && !regexp.test(control.value)) {
        return {'invalid-characters': 'dcc.commons.validation.constraints.latin-characters'};
      }
      return null;
    }
  }

  static charactersOnly(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: string } | null => {

      let regexp: RegExp = /^[\u0600-\u06EF,A-Z, a-z]*$/g;

      if (control.value !== undefined && !regexp.test(control.value)) {
        return {'characters-only': 'dcc.commons.validation.constraints.characters-only'};
      }
      return null;
    }
  }

  static mobileNumber(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: string } | null => {

      let regexp: RegExp = /^0?5\d{8}$/g;

      if (control.value !== undefined && !regexp.test(control.value)) {
        return {'invalid-mobile-number': 'dcc.commons.validation.constraints.mobile-number'};
      }
      return null;
    }
  }

  static ninOrIqama(type: IdType): ValidatorFn {
    return (control: AbstractControl): { [key: string]: string } | null => {

      let regexpNinOnly: RegExp = /^[1]\d{9}$/g;
      let regexpIqamaOnly: RegExp = /^[2]\d{9}$/g;
      let regexpNinOrIqama: RegExp = /^[1-2]\d{9}$/g;

      let regexp: RegExp;

      switch (type) {
        case IdType.NIN_ONLY:
          regexp = regexpNinOnly;
          if (!control.value || !regexp.test(control.value)) {
            return {'invalid-nin-or-iqama' : 'dcc.commons.validation.constraints.nin'};
          }
          break;
        case IdType.IQAMA_ONLY:
          regexp = regexpIqamaOnly;
          if (!control.value || !regexp.test(control.value)) {
            return {'invalid-nin-or-iqama' : 'dcc.commons.validation.constraints.iqama-number'};
          }
          break;
        case IdType.NIN_OR_IQAMA:
          regexp = regexpNinOrIqama;
          if (!control.value || !regexp.test(control.value)) {
            return {'invalid-nin-or-iqama' : 'dcc.commons.validation.constraints.nin-or-iqama-number'};
          }
          break;
      }

      return null;

    }
  }

}
