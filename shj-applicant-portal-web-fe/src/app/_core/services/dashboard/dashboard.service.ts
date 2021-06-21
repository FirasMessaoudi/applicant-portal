import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {DashboardVo} from "@model/dashboard-vo.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) {
  }

  /**
   * Load dashboard data for logged in user agency
   */
  loadData(): Observable<DashboardVo> {
    return this.http.get<DashboardVo>('/core/api/dashboard');
  }

  /**
   * Loads dashboard statistics for a specific period
   *
   * @param periodType     the period type to load
   */
  loadPeriodData(periodType: string): Observable<DashboardVo> {
    return this.http.get<DashboardVo>('/core/api/dashboard/period/' + periodType);
  }

}
