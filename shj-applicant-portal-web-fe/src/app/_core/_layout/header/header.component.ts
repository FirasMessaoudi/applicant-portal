import {Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService, CardService, UserService} from '@app/_core/services';
import {I18nService} from "@dcc-commons-ng/services";
import {Location} from "@angular/common";
import {$animations} from "@shared/animate/animate.animations";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {OtpStorage} from "@pages/otp/otp.storage";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import {NotificationService} from "@core/services/notification/notification.service";
import {UserNewNotificationsCount} from "@model/user-new-notifications-count.model";
import * as momentjs from 'moment';
import * as moment_ from 'moment-hijri';

import {PerfectScrollbarComponent, PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';
import {UtilityService} from "@core/utilities/utility.service";
import {ApplicantRitualPackage} from "@model/applicant-ritual-package.model";
import {Subscription, timer} from "rxjs";

const momentHijri = moment_;

const moment = momentjs;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  host: {'class': 'dcc__wrapper'},
  animations: $animations
})
export class HeaderComponent implements OnInit, OnDestroy {

  closeResult = '';
  currentUser: any;

  @Input()
  showNavbar = false;

  isActive: boolean;
  routerDisabled = true;
  selectedApplicantRitualPackage: ApplicantRitualPackage;
  latestApplicantRitualPackage: ApplicantRitualPackage;
  ritualTypes: Lookup[] = [];
  applicantRitualPackages: ApplicantRitualPackage[];
  showAlert: boolean;
  currentHijriYear: number;
  notifications: DetailedUserNotification[] = [];
  newNotificationsCountTimerInterval: any;
  userNewNotificationsCount: UserNewNotificationsCount;
  activeId = 1;
  private notificationSubscription: Subscription;

  constructor(
    private modalService: NgbModal,
    private notificationService: NotificationService,
    private location: Location,
    public router: Router,
    private i18nService: I18nService,
    private authenticationService: AuthenticationService,
    private el: ElementRef,
    public userService: UserService,
    private cardService: CardService,
    private otpStorage: OtpStorage,
    private lookupsService: LookupService,
    private utilityService: UtilityService
  ) {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.loadUserNotifications();
    this.notificationService.currentUserNotifications.subscribe(refreshedNotifications => {
      this.notifications = refreshedNotifications;
    });
    this.loadUserNewNotificationsCounts();
    this.notificationService.currentUserNewNotificationsCount.subscribe(updatedCount => {
      this.userNewNotificationsCount = updatedCount;
    });
    this.initializeUserNewNotificationsCountTimer();
    this.loadLookups();
    this.listRitualSeasons();
    this.getCurrentHijriYear();
    this.currentUser = this.authenticationService.currentUser;

    this.otpStorage.applicantRitualPackageSubject.subscribe(data => {
      if (data) {
        this.selectedApplicantRitualPackage = data;
        this.latestApplicantRitualPackage = data;
      }
    });

    this.selectedApplicantRitualPackage = JSON.parse((localStorage.getItem('selectedApplicantRitualPackage')));
    this.latestApplicantRitualPackage = JSON.parse((localStorage.getItem('latestApplicantRitualPackage')));

    if (this.selectedApplicantRitualPackage?.applicantPackageId && this.latestApplicantRitualPackage?.applicantPackageId) {
      this.showAlert = this.selectedApplicantRitualPackage?.applicantPackageId !== this.latestApplicantRitualPackage?.applicantPackageId;
    }
  }

  initializeUserNewNotificationsCountTimer() {
    this.notificationService
      .getNewNotificationsInterval()
      .subscribe((timeInterval) => {
        this.notificationSubscription = timer(0, timeInterval).subscribe(() => {
          this.loadUserNewNotificationsCounts();
          this.loadUserNotifications();
        });
      });
  }

  loadUserNotifications() {
    this.notificationService.getNotifications().subscribe(notifications => {
      this.notificationService.updateUserNotifications(notifications);
    });
    this.notificationService.getTypedNotifications("ALL", 0).subscribe(page => {
      this.notificationService.updateUserPaginatedNotifications(page.content);
    });
    this.notificationService.getTypedNotifications("USER_SPECIFIC", 0).subscribe(page => {
      this.notificationService.updateUserSpecificNotifications(page.content);
    });
    this.notificationService.getTypedNotifications("GENERAL", 0).subscribe(page => {
      this.notificationService.updateUserNotSpecificNotifications(page.content);
    });
  }

  loadUserNewNotificationsCounts() {
    this.notificationService.countUserNewNotifications().subscribe(notificationsCount => {
      this.notificationService.updateUserNewNotificationsCount(notificationsCount);
    });
  }

  loadLookups() {
    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  viewCurrentUserProfile() {
    const userId = this.authenticationService.currentUser.id;
    this.router.navigate(["/users/details/" + userId], {queryParams: {myProfile: true}});
  }

  isSupervisor(): boolean {
    return true;
  }

  goBack() {
    this.location.back();
  }

  open(content) {
    this.applicantRitualPackages.forEach(s => s.applicantPackageId === this.selectedApplicantRitualPackage.applicantPackageId ? s.selected = true : s.selected = false);
    this.modalService.open(content, {
      ariaLabelledBy: 'modal-basic-title',
      centered: true,
      size: 'lg'
    }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      this.applicantRitualPackages.forEach(s => s.applicantPackageId === this.selectedApplicantRitualPackage.applicantPackageId ? s.selected = true : s.selected = false);
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  loadNotifications() {
    this.loadUserNewNotificationsCounts();
    this.notificationService.getNotifications().subscribe(data => {
      this.notifications = data;
    });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  listRitualSeasons() {
    this.userService.getListApplicantRitualPackage().subscribe(data => {
      this.applicantRitualPackages = data;
    });
  }

  confirm() {
    this.selectedApplicantRitualPackage = this.applicantRitualPackages.find(s => s.selected === true);
    this.showAlert = this.selectedApplicantRitualPackage.applicantPackageId !== this.latestApplicantRitualPackage.applicantPackageId;
    localStorage.setItem('selectedApplicantRitualPackage', JSON.stringify(this.selectedApplicantRitualPackage));
    localStorage.setItem('selectedApplicantRitualPackage', JSON.stringify(this.selectedApplicantRitualPackage));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitualPackage);
    this.modalService.dismissAll();
  }

  backToLatestRitualSeason() {
    this.selectedApplicantRitualPackage = JSON.parse(localStorage.getItem('latestApplicantRitualPackage'));
    this.applicantRitualPackages.forEach(s => s.applicantPackageId === this.selectedApplicantRitualPackage.applicantPackageId ? s.selected = true : s.selected = false);
    localStorage.setItem('selectedApplicantRitualPackage', JSON.stringify(this.selectedApplicantRitualPackage));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitualPackage);
    this.showAlert = false;
  }

  closeModal() {
    this.modalService.dismissAll();
  }

  selectSeason(season) {
    this.applicantRitualPackages.forEach(s => s.applicantPackageId === season ? s.selected = true : s.selected = false);
  }

  getRelativeTime(date: Date) {
    let dateValue = moment(date);
    dateValue.locale('en');
    if (this.currentLanguage.startsWith('ar')) {
      dateValue.locale('ar-TN').fromNow();
    }
    return dateValue.fromNow();
  }

  private getCurrentHijriYear() {
    let now = moment(new Date());
    this.currentHijriYear = momentHijri(now).iYear();
  }

  getPrivateNotifications() {
    return this.notifications.filter(notification => notification.userSpecific);
  }

  getPublicNotifications() {
    return this.notifications.filter(notification => !notification.userSpecific);
  }

  navigateToNotificationsPage() {
    this.router.navigate(['/notifications']);
  }

  public config: PerfectScrollbarConfigInterface = {};
  @ViewChild(PerfectScrollbarComponent) componentRef?: PerfectScrollbarComponent;

  public scrollToXY(x: number, y: number): void {
    this.componentRef.directiveRef.scrollTo(x, y, 500);
  }

  public scrollToTop(): void {
    this.componentRef.directiveRef.scrollToTop();
  }

  public scrollToLeft(): void {
    this.componentRef.directiveRef.scrollToLeft();
  }

  public scrollToRight(): void {
    this.componentRef.directiveRef.scrollToRight();
  }

  public scrollToBottom(): void {
    this.componentRef.directiveRef.scrollToBottom();
  }

  public onScrollEvent(event: any): void {
    console.log(event);
  }

  ngOnDestroy(): void {
    clearInterval(this.newNotificationsCountTimerInterval);
    if (this.notificationSubscription) {
      this.notificationSubscription.unsubscribe();
    }
  }

  getNotificationCount(){
    let nb = this.userNewNotificationsCount?.userSpecificNewNotificationsCount + this.userNewNotificationsCount?.userNotSpecificNewNotificationsCount;
    if(nb>99){
      return '99+';
    }else {
      return nb;
    }
  }

  getHijriDate(date: Date, seasonYear: number){
    let dateString = this.utilityService.GetHijriDate(new Date(date));
    console.log(dateString);
    return dateString;
  }
}
