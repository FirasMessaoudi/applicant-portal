import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Lookup} from "@model/lookup.model";
import {catchError} from "rxjs/internal/operators";
import {ApplicantComplaint} from "@model/applicant-complaint.model";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class ComplaintService {

  constructor(private http: HttpClient, private cookieService: CookieService) {
  }

  // /**
  //  * list all complaints.
  //  */
  // list(pageNumber: any, complaintSearchCriteria: ComplaintSearchCriteria): Observable<any> {
  //   let params = new HttpParams()
  //     .set('page', pageNumber);
  //   return this.http.post<any>('/core/api/complaints/list', complaintSearchCriteria, {params: params});
  //   //return this.http.get<any>('/core/api/complaints/list');
  // }

  /**
   * load all complaint types.
   */
  findComplaintTypes(): Observable<Lookup[]> {
    return this.http.get<any>('/core/api/lookup/complaint-type/list');
  }

  /**
   * load all complaint statuses.
   */
  findComplaintStatuses() {
    return this.http.get<any>('/core/api/lookup/complaint-sts/list');
  }

  // /**
  //  * Finds complaint by its ID from the server.
  //  *
  //  *@param complaintId the complaint id
  //  * @return {Observable<ApplicantComplaint>} The complaint identified by complaintId.
  //  */
  // find(complaintId: number): Observable<ApplicantComplaint> {
  //   return this.http.get<any>('/core/api/complaints/find/' + complaintId).pipe(
  //     catchError(
  //       (error: any, caught: Observable<HttpEvent<any>>) => {
  //         console.error(error);
  //         return of(null);
  //       }
  //     )
  //   );
  // }

  /**
   * Create complaint.
   *
   * @param complaint the complaint info
   * @param file the file
   */
  createNewComplaint(complaint: ApplicantComplaint, file: any): Observable<any> {
    let formData: FormData = new FormData();
    formData.append("complaint", new Blob([JSON.stringify(complaint)], {
      type: 'application/json'
    }));
    formData.append("attachment", file);
    const req = new HttpRequest('POST', '/core/api/complaints/create', formData, {
      headers: new HttpHeaders({"Content-Type": "multipart/form-data"}),
      reportProgress: true,
      responseType: 'json'
    });
    return this.http.request(req);
  }
  //
  // /**
  //  * Download complaint attachment.
  //  */
  // downloadComplaintAttachment(id) {
  //   return this.http.get('/core/api/complaints/attachments/' + id, {responseType: 'blob'});
  // }

}
