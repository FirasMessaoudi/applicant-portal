import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpParams} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Card} from "@model/card.model";
import {catchError} from "rxjs/internal/operators";
import {Lookup} from "@model/lookup.model";
import {CountryLookup} from "@model/country-lookup.model";

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
