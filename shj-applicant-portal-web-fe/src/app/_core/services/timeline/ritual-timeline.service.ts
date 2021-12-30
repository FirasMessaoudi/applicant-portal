import {Injectable} from "@angular/core";
import {HttpClient, HttpEvent} from "@angular/common/http";
import {catchError} from "rxjs/internal/operators";
import {BehaviorSubject, Observable, of} from "rxjs";
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {Lookup} from "@model/lookup.model";

@Injectable({
  providedIn:"root"
})
export class RitualTimelineService{
  private baseUrl:string
  private dummyUrl:string= '/core/api/users/tafweej/'
  public ritualStepSubject: BehaviorSubject<any> = new BehaviorSubject<any>({});

  constructor(private http:HttpClient) {
    this.ritualStepSubject.asObservable()
  }

  loadRitualSteps(ritualId: number):Observable<CompanyRitualMainDataStep[]> {
    return this.http.get<any>(this.dummyUrl + ritualId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);

          return of(null);
        }
      )
    );
  }

  loadRitualStepsLookups():Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/company_ritual_step/list');
  }

  loadRitualTypes():Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/ritual-type/list');
  }

  getRitualStepSubject(data: any ){
    this.ritualStepSubject.next(data)
  }

}
