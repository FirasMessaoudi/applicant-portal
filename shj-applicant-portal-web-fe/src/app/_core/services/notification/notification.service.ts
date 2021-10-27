import {Injectable} from '@angular/core';
import {Observable, of} from "rxjs";
import {catchError} from "rxjs/internal/operators";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";


/**
 * Provides a base for notification operations.
 */
@Injectable()
export class NotificationService {

  constructor(private http: HttpClient) {

  }

  markAsRead(notificationId: number): Observable<any> {
    return this.http.post('/core/api/notification/mark-as-read/' + notificationId, null)
      .pipe(catchError((error: HttpErrorResponse) => {

        if (error.hasOwnProperty('error')) {

          return of(error.error);
        } else {
          console.error('An error happen while mark notification as read : ' + error);
          return of(error);
        }
      }));
  }

  getNotifications(): Observable<DetailedUserNotification[]> {
    return this.http.get<DetailedUserNotification[]>('/core/api/users/notifications');
  }

  /**
   * Count user new notifications.
   */
  countUserNewNotifications() : Observable<any> {
    return this.http.get('/core/api/notification/new-notifications-count');
  }

}
