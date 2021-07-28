import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";
import {AuthenticationService} from '../services/authentication/authentication.service';


export const PUBLIC_URL_PATTERNS: Array<string> = ['/login', '/otp', '/register', '/reset-password', '/support', '/register-success'];
export const LOGIN_URL: string = '/login';

@Injectable({providedIn: 'root'})
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
  }

  /**
   * Check if the user is logged in before calling http
   *
   * @param route
   * @param state
   * @returns {boolean}
   */
  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (this.authenticationService.isAuthenticated()) {
      if (this.authenticationService.userHasExpiredPassword()) {
        console.log('redirecting to change password :::::: ');
        // user password has expired so redirect him to change-password page
        this.router.navigate(['/change-password'], {replaceUrl: true});
      }
      console.log('is allowed :::::: ');
      return true;
    }
    console.log('redirecting to login :::::: ');
    this.router.navigate([LOGIN_URL], {replaceUrl: true});
    return false;
  }

}
