import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {User} from '@model/user.model';


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
        return of(error);;
      }
    }));
  }
}
