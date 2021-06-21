import { Routes } from '@angular/router';

export const LOGIN_ROUTES: Routes = [
  {
    path: 'login',
    loadChildren: () => import('@pages/login/login.module').then(m => m.LoginModule)
  },
  {
    path: 'otp',
    loadChildren: () => import('@pages/otp/otp.module').then(m => m.OtpModule)
  },
  {
    path: 'reset-password',
    loadChildren: () => import('@pages/reset-password/reset-password.module').then(m => m.ResetPasswordModule)
  },
 
  {
    path: 'change-password',
    loadChildren: () => import('@pages/change-password/change-password.module').then(m => m.ChangePasswordModule)
  }
];


export const REGISTER_ROUTES: Routes = [
  {
    path: 'register',
    loadChildren: () => import('@pages/register/register.module').then(m => m.RegisterModule)
  }
];