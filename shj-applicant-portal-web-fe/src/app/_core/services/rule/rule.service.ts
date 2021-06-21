import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/internal/operators";
import {DecisionRule} from "@model/decision-rule.model";

@Injectable({
  providedIn: 'root'
})
export class RuleService {

  constructor(private http: HttpClient) {
  }

  /**
   * list all rules.
   */
  list(pageNumber: any): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);
    return this.http.get("/core/api/rules/list", {params: params});
  }

  /**
   * Finds rule by its ID from the server.
   *
   *@param ruleId the rule id
   * @return {Observable<Role>} The rule identified by ruleId.
   */
  find(ruleId: number): Observable<DecisionRule> {
    return this.http.get<any>('/core/api/rules/find/' + ruleId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Creates or updates rule details in the server.
   *
   * @param rule the rule to save or update
   * @return {Observable<DecisionRule>} The saved or updated rule.
   */
  saveOrUpdate(rule: DecisionRule): Observable<any> {
    return this.http.post<any>('/core/api/rules/save-or-update', rule).pipe(catchError((error: HttpErrorResponse) => {
        if (error.hasOwnProperty('error')) {
          return of(error.error);
        } else {
          console.error('An error happened while saving the rule : ' + error);
          return of(error);
        }
      })
    );
  }

}
