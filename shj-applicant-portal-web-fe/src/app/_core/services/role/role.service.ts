import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Role} from "@model/role.model";
import {catchError} from "rxjs/internal/operators";
import {Authority} from "@model/authority.model";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private http: HttpClient) {
  }

  listAll(): Observable<any> {
    return this.http.get("/core/api/roles/list/all");
  }

  /**
   * list active roles.
   */
  listActive(): Observable<any> {
    return this.http.get("/core/api/roles/list/active");
  }

  listPaginated(pageNumber: any): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);

    return this.http.get<any>("/core/api/roles/list/paginated", {params: params});
  }

  /**
   * Finds role by its ID from the server.
   *
   *@param roleId the role id
   * @return {Observable<Role>} The role identified by roleId.
   */
  find(roleId: number): Observable<Role> {
    return this.http.get<any>('/core/api/roles/find/' + roleId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Creates or updates role details in the server.
   *
   * @param role the role to save or update
   * @return {Observable<Role>} The saved or updated role.
   */
  saveOrUpdate(role: Role): Observable<any> {
    return this.http.post<any>('/core/api/roles/'+ (role.id > 0 ? 'update' : 'create'), role).pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          console.error('An error happened while saving the role : ' + error);
          return of(error);
        }
      })
    );
  }

  listAuthorities(): Observable<Authority[]> {
    return this.http.get<any>('/core/api/lookup/authority/list/parent');
  }

  /**
   * Deletes role by his ID from the server.
   *
   * @param roleId the role id
   * @return {Observable} The role identified by roleId.
   */
  delete(roleId: number): Observable<any> {
    return this.http.post('/core/api/roles/delete/' + roleId, null);
  }

  /**
   * Deactivates role by his ID from the server.
   *
   * @param roleId the role id
   */
  deactivate(roleId: number): Observable<any> {
    return this.http.post("/core/api/roles/deactivate/" + roleId, null).pipe(catchError((error: HttpErrorResponse) => {
        console.error('An error happened while deactivating role : ' + error);
        return of(error);
      })
    );
  }

  /**
   * Activates role by his ID from the server.
   *
   * @param roleId the role id
   */
  activate(roleId: number): Observable<any> {
    return this.http.post("/core/api/roles/activate/" + roleId, null).pipe(catchError((error: HttpErrorResponse) => {
        console.error('An error happened while activating role : ' + error);
        return of(error);
      })
    );
  }

  search(authorityId: any, roleNameAr: any, roleNameEn: any): Observable<any> {
    return this.http.get('/core/api/roles/search?authorityId=' + (authorityId? authorityId : -1)
      + '&arabicName=' + (roleNameAr ? roleNameAr : '') + '&englishName=' + (roleNameEn ? roleNameEn : ''));
  }
}
