import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpParams} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Card} from "@model/card.model";
import {catchError} from "rxjs/internal/operators";
import {Lookup} from "@model/lookup.model";
import {CountryLookup} from "@model/country-lookup.model";
import {ApplicantMainData} from "@model/applicant-main-data.model";
import {ApplicantHealth} from "@model/applicant-health.model";
import {ApplicantRitualCard} from "@model/applicant-ritual-card";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor(private http: HttpClient) {
  }

  list(pageNumber: any): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);
    return this.http.get<any>("/core/api/cards/list", {params: params});
  }

  /**
   * Finds card by its ID from the server.
   *
   * @param cardId the card id
   * @return {Observable<Card>} The card identified by cardId.
   */
  find(cardId: number): Observable<Card> {
    return this.http.get<any>('/core/api/cards/find/' + cardId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Finds main profile data
   *
   * @return {Observable<ApplicantMainData>} The card identified by cardId.
   */
  findMainProfile(ritualId): Observable<ApplicantMainData> {
    return this.http.get<any>('/core/api/users/main-data/'+ritualId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Finds user health details
   *
   * @return {Observable<ApplicantHealth>}
   */
  findHealthDetails(ritualId: number): Observable<ApplicantHealth> {
    return this.http.get<any>('/core/api/users/health/' + ritualId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Finds user card details
   *
   * @return {Observable<ApplicantRitualCard>}
   */
  findCardDetails(ritualId: number): Observable<ApplicantRitualCard> {
    return this.http.get<any>('/core/api/users/details/' + ritualId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  findRitualTypes(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/ritual-type/list');
  }

  findCardStatuses(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/card-status/list');
  }

  findRelativeRelationships(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/relative-relationship/list');
  }

  findCountries(): Observable<CountryLookup[]> {
    return this.http.get<any>('/core/api/lookup/country/list');
  }

  findHealthSpecialNeeds(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/health-special-needs/list');
  }

  findMaritalStatuses(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/marital-status/list');
  }
}
