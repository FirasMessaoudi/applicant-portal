import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {DataRequest, Role} from "@shared/model";
import {catchError} from "rxjs/internal/operators";

@Injectable({
  providedIn: 'root'
})
export class DataRequestService {

  constructor(private http: HttpClient) {
  }

  /**
   * list all data requests.
   */
  list(pageNumber: any): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);
    return this.http.get("/core/api/data/request/list", {params: params});
  }

  /**
   * Finds data request by its ID from the server.
   *
   *@param dataRequestId the data request id
   * @return {Observable<DataRequest>} The data request identified by dataRequestId.
   */
  find(dataRequestId: number): Observable<DataRequest> {
    return this.http.get<any>('/core/api/data/request/find/' + dataRequestId).pipe(
      catchError(
        (error: any, caught: Observable<HttpEvent<any>>) => {
          console.error(error);
          return of(null);
        }
      )
    );
  }

  /**
   * Downloads a template for a specific segment
   *
   * @param segmentId data segment Id
   * @return the template for the given segment
   */
  downloadTemplate(segmentId: number): Observable<any> {
    return this.http.get('/core/api/data/request/tpl/' + segmentId, {
      responseType: 'blob' as 'json',
      observe: 'response' as 'body'
    });
  }


  /**
   * Downloads a template for a specific segment
   *
   * @param dataRequestId data request Id
   * @param fileType file type to download
   * @return the file for the given data request and file type
   */
  downloadFile(dataRequestId: number, fileType:String): Observable<any> {
    return this.http.get('/core/api/data/request/' + dataRequestId + '/file/' + fileType, {
      responseType: 'blob' as 'json',
      observe: 'response' as 'body'
    });
  }

  /**
   * Creates a new data request
   *
   * @param dataRequest the data request
   * @return the persisted request updated
   */
  create(dataRequest: DataRequest, file: any): Observable<HttpEvent<any>> {
    let formData: FormData = new FormData();
    formData.append("request", new Blob([JSON.stringify(dataRequest)], {
      type: 'application/json'
    }));
    formData.append("file", file);
    const req = new HttpRequest('POST', '/core/api/data/request/create', formData, {
      headers: new HttpHeaders({"Content-Type": "multipart/form-data"}),
      reportProgress: true,
      responseType: 'json'
    });
    return this.http.request(req);
  }

  /**
   * Confirms a newly created data request
   *
   * @param dataRequestId the data request id to be confirmed
   */
  confirm(dataRequestId: number): Observable<any> {
    return this.http.post('/core/api/data/request/confirm/' + dataRequestId, null);
  }

  /**
   * Returns the css class for the given status
   *
   * @param status the current data request status
   */
  buildStatusClass(status: any): string {
    switch (status.id) {
      case 1:
        return "new";
      case 2:
        return "ready";
      case 3:
        return "warning";
      case 4:
        return "done";
      case 5:
        return "done-with-errors";
      case 6:
        return "Suspended";
      default:
        return "new";
    }
  }

}
