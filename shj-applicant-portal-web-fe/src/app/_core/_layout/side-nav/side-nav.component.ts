import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {EAuthority} from "@model/enum/authority.enum";
import {AuthenticationService} from '@app/_core/services';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss']
})
export class SideNavComponent implements OnInit {
  currentUser: any;
  links: {}[];
  constructor(
    public router: Router,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    this.currentUser = this.authenticationService.currentUser
    this.links = [
      {
        title: 'home.title',
        roles: [EAuthority.ADMIN_DASHBOARD],
        display: false,
        icon: 'portrait',
        iconFa:'fa-w-12',
        routerLink: '/',
      },
      {
        title: 'card-management.title',
        roles: [EAuthority.USER_MANAGEMENT],
        display: false,
        icon: 'address-card',
        iconFa:'fa-w-18',
        routerLink: '/cards/list',
      },
      {
        title: 'printing-management.title',
        roles: [EAuthority.USER_MANAGEMENT],
        display: false,
        icon: 'print',
        iconFa:'fa-w-16',
        routerLink: '/print-requests/list',
      },
      {
        title: 'role-management.title',
        roles: [EAuthority.ROLE_MANAGEMENT],
        display: false,
        icon: 'file-alt',
        iconFa:'fa-w-12',
        routerLink: '/roles/list',
      },
      {
        title: 'user-management.title',
        roles: [EAuthority.USER_MANAGEMENT],
        display: false,
        icon: 'users',
        iconFa:'fa-w-20',
        routerLink: '/users/list',
      },
      {
        title: 'rule-management.title',
        roles: [EAuthority.USER_MANAGEMENT],
        display: false,
        icon: 'users',
        iconFa:'fa-w-20',
        routerLink: '/rules/list',
      },
      {
        title: 'data-request-management.title',
        roles: [EAuthority.USER_MANAGEMENT],
        display: false,
        icon: 'users',
        iconFa:'fa-w-20',
        routerLink: '/data-requests/list',
      }
    ];
    // filtering access according to connected user authorities
    let user: any = this.authenticationService.currentUser;

    if (user && user.authorities && (user.authorities instanceof Array)) {
      // loop on links
      this.links.forEach((link: any) => {
        // loop on authorities
        user.authorities.forEach((auth: any) => {
          // loop on link roles
          link.roles.forEach((role: any) => {

            if (role == auth.authority) {
              link['display'] = true;
            }
          });
        });
      });
    }
  }

}
