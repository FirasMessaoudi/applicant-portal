import {Component, ElementRef, Input, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService, CardService, UserService} from '@app/_core/services';
import {I18nService} from "@dcc-commons-ng/services";
import {Location} from "@angular/common";
import {$animations} from "@shared/animate/animate.animations";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {OtpStorage} from "@pages/otp/otp.storage";
import {CompanyRitualSeasonLite} from "@model/company-ritual-season-lite.model";
import {DetailedUserNotification} from "@model/detailed-user-notification.model";
import {NotificationService} from "@core/services/notification/notification.service";

import * as momentjs from 'moment';
import * as moment_ from 'moment-hijri';

import { PerfectScrollbarConfigInterface,
  PerfectScrollbarComponent, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';

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
  selectedApplicantRitual: ApplicantRitualLite;
  selectedRitualSeason: CompanyRitualSeasonLite;
  latestRitualSeason: CompanyRitualSeasonLite;
  ritualTypes: Lookup[] = [];
  seasons: CompanyRitualSeasonLite[];
  showAlert: boolean;
  currentHijriYear: number;
  notifications: DetailedUserNotification[] = [];
  newNotificationsCountTimerInterval: any;
  newNotificationsCount: number;
  activeId = 1;

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
    private lookupsService: LookupService) {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.countUserNewNotifications();
    this.loadLookups();
    this.listRitualSeasons();
    this.getCurrentHijriYear();
    this.currentUser = this.authenticationService.currentUser;

    this.otpStorage.ritualSeasonSubject.subscribe(data => {
      if (data) {
        this.selectedRitualSeason = data;
        this.latestRitualSeason = data;
      }
    });

    this.selectedRitualSeason = JSON.parse((localStorage.getItem('selectedRitualSeason')));
    this.latestRitualSeason = JSON.parse((localStorage.getItem('latestRitualSeason')));

    if (this.selectedRitualSeason?.id && this.latestRitualSeason?.id) {
      this.showAlert = this.selectedRitualSeason?.id !== this.latestRitualSeason?.id;
    }
  }

  countUserNewNotifications() {
    this.newNotificationsCountTimerInterval = setInterval(() => {
      // call back-end to get the count
      this.notificationService.countUserNewNotifications().subscribe(count => {
        this.newNotificationsCount = count;
      });
    }, 120000);
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
    this.seasons.forEach(s => s.id === this.selectedRitualSeason.id ? s.selected = true : s.selected = false);
    this.modalService.open(content, {
      ariaLabelledBy: 'modal-basic-title',
      centered: true,
      size: 'lg'
    }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      this.seasons.forEach(s => s.id === this.selectedRitualSeason.id ? s.selected = true : s.selected = false);
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
    this.notificationService.getNotifications().subscribe(data => {
      this.notifications = data;
    });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  listRitualSeasons() {
    this.userService.listRitualSeasons().subscribe(data => {
      this.seasons = data;
    });
  }

  confirm() {
    this.selectedRitualSeason = this.seasons.find(s => s.selected === true);
    this.showAlert = this.selectedRitualSeason.id !== this.latestRitualSeason.id;
    localStorage.setItem('selectedRitualSeason', JSON.stringify(this.selectedRitualSeason));
    localStorage.setItem('selectedRitualSeason', JSON.stringify(this.selectedRitualSeason));
    this.userService.changeSelectedApplicantRitual(this.selectedRitualSeason);
    this.modalService.dismissAll();
  }

  backToLatestRitualSeason() {
    this.selectedRitualSeason = JSON.parse(localStorage.getItem('latestRitualSeason'));
    this.seasons.forEach(s => s.id === this.selectedRitualSeason.id ? s.selected = true : s.selected = false);
    localStorage.setItem('selectedRitualSeason', JSON.stringify(this.selectedRitualSeason));
    this.userService.changeSelectedApplicantRitual(this.selectedRitualSeason);
    this.showAlert = false;
  }

  closeModal() {
    this.modalService.dismissAll();
  }

  selectSeason(season) {
    this.seasons.forEach(s => s.id === season ? s.selected = true : s.selected = false);
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
  }
}
