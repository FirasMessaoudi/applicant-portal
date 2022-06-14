import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {User} from '@model/user.model';
import {ValidateApplicantCmd} from "@model/validate-applicant-cmd.model";


/**
 * Provides operations for user registration.
 */
@Injectable()
export class RegisterService {

  constructor(private http: HttpClient) {
  }

  validateOtpThenRegister(user: User, pin: string): Observable<any> {
    return this.http.post(`/core/api/register?pin=${pin}`, user);
  }

  generateOTPForRegistration(user: User, phonePrefix: string, recaptchaToken: string): Observable<any> {
    user.countryPhonePrefix = phonePrefix;
    console.log(user);
    return this.http.post<any>('/core/api/register/otp?grt=' + recaptchaToken, user)
      .pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          return of(error);
        }
      }));
  }

  verifyApplicant(loginType:string, identifier:any,dateOfBirthGregorian:any,dateOfBirthHijri:any, nationality: any): Observable<any>{
    return this.http.post<any>("/core/api/register/verify", {'type': loginType, 'identifier': identifier,
    'nationalityCode': nationality, 'dateOfBirthGregorian':dateOfBirthGregorian, 'dateOfBirthHijri':dateOfBirthHijri});
   }
}
