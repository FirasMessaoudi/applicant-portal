import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Authority} from "@shared/model";

/**
 * Provides a base for applicant operations.
 */
@Injectable()
export class ApplicantService {

  constructor(private http: HttpClient) {
  }

  listRitualTypes(): Observable<Authority[]> {
    return this.http.get<any>('/core/api/lookup/ritual-type/list');
  }
}
