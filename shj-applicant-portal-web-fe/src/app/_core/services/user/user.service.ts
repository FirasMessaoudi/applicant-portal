import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError} from "rxjs/internal/operators";
import {ChangePasswordCmd, User} from '@shared/model';
import {Role} from '@shared/model/role.model';
import {UserStatus} from "@model/user-status.model";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {UserContacts} from "@model/UserContacts.model";
import {CookieService} from "ngx-cookie-service";
import {ApplicantRitualPackage} from "@model/applicant-ritual-package.model";

export const DEFAULT_MAX_USER_AGE = 16;

/**
 * Provides a base for user operations.
 */
@Injectable()
export class UserService {

  public selectedApplicantRitualPackage: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  seasons: number[] = [];
  applicantRituals: ApplicantRitualLite[] = [];

  constructor(private http: HttpClient, private cookieService: CookieService) {
    this.selectedApplicantRitualPackage.asObservable();
  }

  public changeSelectedApplicantRitual(selectedApplicantRitualPackage: any) {
    this.selectedApplicantRitualPackage.next(selectedApplicantRitualPackage);
  }

  /**
   * Lists all users.
   *
   * @return {Observable<Page>} The users list.
   */
  list(pageNumber: any): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);

    return this.http.get<any>('/core/api/users/list', {params: params});
  }

  /**
   * Finds user by his ID from the server.
   *
   *@param userId the user id
   * @return {Observable<User>} The user identified by userId.
   */
  find(userId: number): Observable<any> {
    return this.http.get<any>('/core/api/users/find/' + userId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  search(pageNumber: any, role: Role, nin: any, status: UserStatus): Observable<any> {

    let params = new HttpParams().set('page', pageNumber);

    return this.http.get('/core/api/users/search/' + (role? role.id : -1)+'/' + (nin? nin : -1)+'/' + (status? (status.activated ? 1: 0) : -1), {params: params});
  }

  /**
   * Upload user avatar to the server.
   * @param file the avatar file
   * @param userId the user id
   * @return {Observable<User>} The user identified by userId.
   */
  uploadAvatar(file: File, userId: any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('avatar', file);

    let headers: HttpHeaders = new HttpHeaders({'Content-Type': 'multipart/form-data'});

    return this.http.post<any>('/core/api/users/avatar/' + userId, formData, {
      headers: headers,
      responseType: 'text' as 'json'
    })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.hasOwnProperty('error')) {
            return of(error.error);
          } else {
            console.error('An error happened while uploading the user avatar: ' + error);
            return of(error);
          }
        })
      );
  }

  /**
   * Creates or updates user details in the server.
   *
   * @param user the user to save or update
   * @return {Observable<User>} The saved or updated user.
   */
  saveOrUpdate(user: User): Observable<any> {
    return this.http.post<any>('/core/api/users/' + (user.id > 0 ? 'update' : 'create'), user).pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          console.error('An error happened while saving the user : ' + error);
          return of(error);
        }
      })
    );
  }

  /**
   *   updates user preferred language.
   *
   * @param lang
   * @param uin
   * @return {Observable<User>} The saved or updated user.
   */
  updatePreferredLang(lang: string, uin: string): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('X-XSRF-TOKEN', this.cookieService.get("XSRF-TOKEN"));
    return this.http.put<any>('/core/api/users/language/' + lang.toLowerCase() + '/' + uin, null,
      {'headers': headers}).pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          console.error('An error happened while updating the user language: ' + error);
          return of(error);
        }
      })
    );
  }

  /**
   * Creates or updates user details in the server.
   *
   * @param userContacts
   * @return {Observable<User>} The saved or updated user.
   */
  updateUserContacts(userContacts: UserContacts): Observable<any> {

    let headers = new HttpHeaders();
    headers = headers.set('X-XSRF-TOKEN', this.cookieService.get("XSRF-TOKEN"));

    return this.http.put<any>('/core/api/users/contacts' , userContacts,{'headers':headers});

  }

  generateOTPForEditContact(userContacts: UserContacts): Observable<any> {

    return this.http.post<any>('/core/api/users/otp', userContacts)
      .pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          return of(error);
        }
      }));
  }

  /**
   * Changes the password of the user identified by id.
   *
   * @param changePasswordCmd
   * @return {Observable<any>} The operation result.
   */
  changePassword(changePasswordCmd: ChangePasswordCmd): Observable<any> {
    return this.http.post<any>('/core/api/users/change-password', changePasswordCmd)
      .pipe(catchError((error: HttpErrorResponse) => {
          if (error.hasOwnProperty('error')) {
            return of(error.error);
          } else {
            console.error('An error happened while changing user password : ' + error);
            return of(error);
          }
        })
      );
  }

  /**
   * Deletes user by his ID from the server.
   *
   * @param userId the user id
   * @return {Observable} The user identified by userId.
   */
  delete(userId: number): Observable<any> {
    return this.http.post('/core/api/users/delete/' + userId, null);
  }

  /**
   * Activate user
   * @param userId
   */
  activate(userId: number): Observable<any> {
    return this.http.post('/core/api/users/activate/' + userId, null);
  }

  /**
   * Deactivate user
   * @param userId
   */
  deactivate(userId: number): Observable<any> {
    return this.http.post('/core/api/users/deactivate/' + userId, null);
  }

  /**
   *
   * @param form
   * @param recaptchaToken
   */
  resetPassword(form: any, recaptchaToken: string): Observable<any> {
    return this.http.post('/core/api/users/reset-password?grt=' + recaptchaToken, form)
      .pipe(catchError((error: HttpErrorResponse) => {

        if (error.hasOwnProperty('error')) {

          return of(error.error);
        } else {
          console.error('An error happen while registering new user : ' + error);
          return of(error);
        }
      }));
  }

  getListApplicantRitualPackage(): Observable<ApplicantRitualPackage[]>{
    return this.http.get<ApplicantRitualPackage[]>('/core/api/users/ritual-package')
  }

  getLatestApplicantRitualPackage(): Observable<ApplicantRitualPackage>{
    return this.http.get<ApplicantRitualPackage>('/core/api/users/ritual-package/latest')
  }


}
