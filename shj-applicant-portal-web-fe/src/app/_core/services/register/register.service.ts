import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse,HttpParams} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {BehaviorSubject, Observable, of} from "rxjs";
import {User} from '@model/user.model';
import {environment} from "@env/environment";
import {ValidateApplicantCmd} from "@model/validate-applicant-cmd.model";


/**
 * Provides operations for user registration.
 */
@Injectable()
export class RegisterService {

  constructor(private http: HttpClient) {
  }

  register(user: User, recaptchaToken: string): Observable<any> {
    return this.http.post('/core/api/register?grt=' + recaptchaToken, user).pipe(
      catchError((error: HttpErrorResponse) => {
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
