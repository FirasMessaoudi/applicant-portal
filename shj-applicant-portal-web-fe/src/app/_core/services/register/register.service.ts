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

  register(user: User,needToUpdateInAdminPortal:boolean): Observable<any> {
    return this.http.post('/core/api/register?uadmin='+needToUpdateInAdminPortal, user).pipe(
      catchError((error: HttpErrorResponse) => {
      if (error.hasOwnProperty('error')) {
        return of(error.error);
      } else {
        return of(error);
      }
    }));
  }

  generateOTPForRegistration(user: User, recaptchaToken: string): Observable<any> {
    return this.http.post<any>('/core/api/register/otp?grt=' + recaptchaToken, user)
      .pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          return of(error);
        }
      }));
  }

  verifyApplicant(uin:any,dateOfBirthGregorian:any,dateOfBirthHijri:any){
    let command = new ValidateApplicantCmd(uin,dateOfBirthGregorian,dateOfBirthHijri);
    return this.http.post<any>("/core/api/register/verify",command);
   }
}
