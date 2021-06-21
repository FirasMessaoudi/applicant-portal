import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DataRequest} from "@shared/model";

@Injectable({
  providedIn: 'root'
})
export class DataSegmentService {

  constructor(private http: HttpClient) {
  }

  /**
   * List all data segments.
   *
   * @return the list of data segments
   */
  list(): Observable<any> {
    return this.http.get("/core/api/data/segment/list");
  }

}
