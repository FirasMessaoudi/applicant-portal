import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  AppLayoutComponent,
  DccLayoutLandingComponent,
  LoginLayoutComponent,
  RegisterLayoutComponent
} from '@core/_layout';
import {APP_ROUTES} from '@core/routes/app-routes';
import {LOGIN_ROUTES} from '@core/routes/auth-routes';

import {QuicklinkModule, QuicklinkStrategy} from 'ngx-quicklink';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {PUBLIC_ROUTES} from "@core/routes/public-routes";
import {CARD_MANAGEMENT_ROUTES} from "@core/routes/card-management-routes";
import {LANDING_ROUTES} from "@core/routes/landing-routes";
import {OTP_PRIVATE_ROUTES} from "@core/routes/otp-private-routes";

const routes: Routes = [

  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [AuthenticationGuard],
    children: APP_ROUTES
  },
  {
    path: '',
    component: LoginLayoutComponent,
    children: LOGIN_ROUTES
  },
  {
    path: '',
    component: LoginLayoutComponent,
    children: PUBLIC_ROUTES
  },
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [AuthenticationGuard],
    children: CARD_MANAGEMENT_ROUTES
  },
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [AuthenticationGuard]
  },
  {
    path: '',
    component: DccLayoutLandingComponent,
    children: LANDING_ROUTES
  },
  {
    path: '',
    component: LoginLayoutComponent,
    canActivate: [AuthenticationGuard],
    children: OTP_PRIVATE_ROUTES
  },
  {
    path: '**',
    redirectTo: '',
  }
];


@NgModule({
  imports: [
    QuicklinkModule,
    RouterModule.forRoot(routes, {preloadingStrategy: QuicklinkStrategy, useHash: true, scrollPositionRestoration: 'enabled' })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
