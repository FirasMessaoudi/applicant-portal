import {Component, ElementRef, Input, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {AuthenticationService} from '@app/_core/services';
import {I18nService} from "@dcc-commons-ng/services";
import {Location} from "@angular/common";
import {$animations} from "@shared/animate/animate.animations";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  host: { 'class': 'dcc__wrapper' },
  animations: $animations
})
export class HeaderComponent implements OnInit {

  currentUser: any;
  @Input()
  showNavbar = false;
  isActive: boolean;
  public isMenuCollapsed = false;

  constructor(
    private location: Location,
    public router: Router,
    private i18nService: I18nService,
    private authenticationService: AuthenticationService,
    private el: ElementRef,) {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  ngOnInit() {
    this.currentUser = this.authenticationService.currentUser
    this.isActive = false;

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
    this.  isActive = !this.isActive
  }


}
