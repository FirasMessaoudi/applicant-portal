import {Injectable} from "@angular/core";
import {
  HttpClient,
  HttpEvent,
  HttpHeaders,
  HttpRequest
} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Lookup} from "@model/lookup.model";
import {catchError} from "rxjs/internal/operators";
import {ApplicantIncident} from "@model/applicant-incident.model";

@Injectable({
  providedIn: 'root'
})
export class IncidentService {

  constructor(private http: HttpClient) {
  }

  /**
   * list all incidents.
   */
  list(): Observable<any> {
    return this.http.get<any>('/core/api/incidents/list');
  }

  /**
   * load all incident types.
   */
  findIncidentTypes(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/incident-type/list');
  }

  /**
   * load all incident statuses.
   */
  findIncidentStatuses() {
    return this.http.get<any>('/core/api/lookup/incident-sts/list');
  }

  /**
   * Finds incident by its ID from the server.
   *
   *@param incidentId the incident id
   * @return {Observable<ApplicantIncident>} The incident identified by incidentId.
   */
  find(incidentId: number): Observable<ApplicantIncident> {
    return this.http.get<any>('/core/api/incidents/find/' + incidentId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Create incident.
   *
   * @param incident the incident info
   * @param file the file
   */
  createNewIncident(incident: ApplicantIncident, file: any): Observable<any> {
    let formData: FormData = new FormData();
    formData.append("incident", new Blob([JSON.stringify(incident)], {
      type: 'application/json'
    }));
    formData.append("attachment", file);
    const req = new HttpRequest('POST', '/core/api/incidents/create', formData, {
      headers: new HttpHeaders({"Content-Type": "multipart/form-data"}),
      reportProgress: true,
      responseType: 'json'
    });
    return this.http.request(req);
  }

  /**
   * Download incident attachment.
   */
  downloadIncidentAttachment(id) {
    return this.http.get('/core/api/incidents/attachments/' + id, {responseType: 'blob'});
  }

}
