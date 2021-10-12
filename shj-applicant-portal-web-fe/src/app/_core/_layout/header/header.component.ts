import {Component, ElementRef, Input, OnInit} from '@angular/core';

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
import * as momentjs from 'moment';

const moment = momentjs;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  host: {'class': 'dcc__wrapper'},
  animations: $animations
})
export class HeaderComponent implements OnInit {
  closeResult = '';
  currentUser: any;
  @Input()
  showNavbar = false;
  isActive: boolean;
  public isMenuCollapsed = false;
  routerDisabled = true;
  selectedApplicantRitual: ApplicantRitualLite;
  selectedRitualSeason: CompanyRitualSeasonLite;
  latestRitualSeason: CompanyRitualSeasonLite;
  ritualTypes: Lookup[] = [];
  seasons: CompanyRitualSeasonLite[];
  showAlert: boolean;
  allNotifications: DetailedUserNotification[] = [];
  privateNotifications: DetailedUserNotification[] = [];
  publicNotifications: DetailedUserNotification[] = [];


  activeId = 1;
  tabsHeader = [
    "notification-management.private_notifications",
    "notification-management.public_notifications",
    "notification-management.view_all"
  ]


  constructor(
    private modalService: NgbModal,
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
    this.loadLookups();
    this.listRitualSeasons();
    this.currentUser = this.authenticationService.currentUser;

    this.otpStorage.ritualSeasonSubject.subscribe(data => {
      if (data) {
        this.selectedRitualSeason = data;
        this.latestRitualSeason = data;
      }
    });

    this.selectedRitualSeason = JSON.parse((localStorage.getItem('selectedRitualSeason')));
    this.latestRitualSeason = JSON.parse((localStorage.getItem('latestRitualSeason')));

    this.showAlert = this.selectedRitualSeason?.id !== this.latestRitualSeason?.id;

    this.loadNotifications();

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
    this.userService.getNotifications().subscribe(data => {
      this.allNotifications = data;
      this.privateNotifications = data.filter(notification => notification.userSpecific);
      this.publicNotifications = data.filter(notification => !notification.userSpecific);
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

}
