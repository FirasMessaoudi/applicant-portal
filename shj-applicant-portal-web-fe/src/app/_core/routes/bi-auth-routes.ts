import {Routes} from '@angular/router';

export const BI_LOGIN_ROUTES: Routes = [
  {
    path: 'login',
    loadChildren: () => import('@pages/bi-login/bi-login.module').then(m => m.LoginModule)
  },
  {
    path: 'otp',
    loadChildren: () => import('@pages/bi-otp/bi-otp.module').then(m => m.OtpModule)
  }
];
