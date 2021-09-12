import {Component, ElementRef, Input, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {AuthenticationService, CardService, UserService} from '@app/_core/services';
import {I18nService} from "@dcc-commons-ng/services";
import {Location} from "@angular/common";
import {$animations} from "@shared/animate/animate.animations";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  host: {'class': 'dcc__wrapper'},
  animations: $animations
})
export class HeaderComponent implements OnInit {

  currentUser: any;
  @Input()
  showNavbar = false;
  isActive: boolean;
  public isMenuCollapsed = false;
  routerDisabled = true;
  selectedApplicantRitual: ApplicantRitualLite;

  ritualTypes: Lookup[] =[];


  selectedSeason: number;
  enableEditRitual = false;

  constructor(
    private location: Location,
    public router: Router,
    private i18nService: I18nService,
    private authenticationService: AuthenticationService,
    private el: ElementRef,
    public userService: UserService,
    private cardService: CardService,
    private lookupsService: LookupService) {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {

    this.currentUser = this.authenticationService.currentUser;
    this.isActive = false;

    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });

    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual=>{
      this.selectedApplicantRitual =selectedApplicantRitual;
    });

    this.selectedSeason = JSON.parse((localStorage.getItem('selectedSeason')));
    this.selectedApplicantRitual =JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    if (this.userService.seasons.length <= 0) {
      this.getRitualSeason();
    }

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

  state = 'normal';
  private navClasses = {
    normal: 'wrapper-collapse',
    collapsed: 'wrapper-expand'
  };

  toggleNavbar() {
    let bodyClass = '';

    if (this.state === 'normal') {
      this.state = 'collapse';
      bodyClass = this.navClasses.collapsed;
    } else {
      this.state = 'normal';
      bodyClass = this.navClasses.normal;
    }
    this.el.nativeElement.closest('body').className = bodyClass;
    this.isActive = !this.isActive
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  getRitualSeason() {
    this.userService.getApplicantSeason().subscribe(seasons => {
      console.log('seasons', seasons);
      this.userService.seasons = seasons;
      if (this.userService.seasons.length > 0) {
        console.log("this.selectedSeason", this.selectedSeason)
        if (this.selectedSeason == null) {
          this.selectedSeason = this.userService.seasons[0];
        }

        localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
        this.getApplicantRitualLiteBySeason(true);
      }
    })
  }

  getApplicantRitualLiteBySeason(firstCall: boolean) {

    if (this.selectedSeason) {
      this.userService.applicantRituals = [];
      this.userService.getApplicantRitualLiteBySeason(this.selectedSeason).subscribe(applicantRituals => {
        this.userService.applicantRituals = applicantRituals;
        if (this.userService.applicantRituals.length > 0) {

          if (firstCall == true && this.selectedApplicantRitual != null) {
            let curSelected = this.userService.applicantRituals.filter(ar => ar.id === this.selectedApplicantRitual.id);
            if (curSelected.length > 0) {
              this.changeSelectedApplicantRitual(this.selectedApplicantRitual);
            } else {
              this.changeSelectedApplicantRitual(this.userService.applicantRituals[0]);
            }
          } else {
            this.changeSelectedApplicantRitual(this.userService.applicantRituals[0]);
          }
        }
      });
    }
  }

  onSeasonChange(event) {
    this.selectedSeason = event.target.value;
    this.getApplicantRitualLiteBySeason(false);
  }

  changeSelectedApplicantRitual(applicantRitual: ApplicantRitualLite) {
    this.selectedApplicantRitual = applicantRitual;
  }


  onApplicantRitualChange(event) {
    let curSelected = this.userService.applicantRituals.filter(ar => ar.id == event.target.value)[0];
    this.changeSelectedApplicantRitual(curSelected);
  }

  saveSelectedRitual() {
    localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
    localStorage.setItem('selectedApplicantRitual', JSON.stringify(this.selectedApplicantRitual));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitual);

    this.enableEditRitual = false;
  }

  editRitual() {
    this.enableEditRitual = true;
  }

  CancelEditRitual(){
    this.enableEditRitual = false;
  }

}
