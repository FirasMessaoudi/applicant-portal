import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from "rxjs";
import {catchError} from "rxjs/internal/operators";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import {UserNewNotificationsCount} from "@model/user-new-notifications-count.model";


/**
 * Provides a base for notification operations.
 */
@Injectable()
export class NotificationService {

  private userNewNotificationsCountBehavior = new BehaviorSubject<UserNewNotificationsCount>(null);
  currentUserNewNotificationsCount = this.userNewNotificationsCountBehavior.asObservable();

  private userNotificationsBehavior = new BehaviorSubject<DetailedUserNotification[]>(null);
  currentUserNotifications = this.userNotificationsBehavior.asObservable();

  private userPaginatedNotificationsBehavior = new BehaviorSubject<DetailedUserNotification[]>(null);
  currentUserPaginatedNotifications = this.userPaginatedNotificationsBehavior.asObservable();

  private userSpecificNotificationsBehavior = new BehaviorSubject<DetailedUserNotification[]>(null);
  currentUserSpecificNotifications = this.userSpecificNotificationsBehavior.asObservable();

  private userNotSpecificNotificationsBehavior = new BehaviorSubject<DetailedUserNotification[]>(null);
  currentUserNotSpecificNotifications = this.userNotSpecificNotificationsBehavior.asObservable();

  constructor(private http: HttpClient) {
  }

  updateUserNotifications(userNotifications: DetailedUserNotification[]) {
    this.userNotificationsBehavior.next(userNotifications);
  }

  updateUserNewNotificationsCount(userNewNotificationsCount: UserNewNotificationsCount) {
    this.userNewNotificationsCountBehavior.next(userNewNotificationsCount);
  }

  updateUserPaginatedNotifications(userPaginatedNotifications: DetailedUserNotification[]) {
    this.userPaginatedNotificationsBehavior.next(userPaginatedNotifications);
  }

  updateUserSpecificNotifications(userSpecificNotifications: DetailedUserNotification[]) {
    this.userSpecificNotificationsBehavior.next(userSpecificNotifications);
  }

  updateUserNotSpecificNotifications(userNotSpecificNotifications: DetailedUserNotification[]) {
    this.userNotSpecificNotificationsBehavior.next(userNotSpecificNotifications);
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
    return this.http.get<DetailedUserNotification[]>('/core/api/notification/list');
  }

  getTypedNotifications(type, pageNumber): Observable<any> {
    let params = new HttpParams().set('page', pageNumber);
    if (type?.length > 0) {
      params = params.append('type', type);
    }
    return this.http.get<DetailedUserNotification[]>('/core/api/notification/list', {params: params});
  }

  /**
   * Count user new notifications.
   */
  countUserNewNotifications(): Observable<any> {
    return this.http.get('/core/api/notification/new-notifications-count');
  }

}
