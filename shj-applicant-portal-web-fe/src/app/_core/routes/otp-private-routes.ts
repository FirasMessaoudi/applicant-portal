import { Routes } from '@angular/router';

export const OTP_PRIVATE_ROUTES: Routes = [
  {
    path: 'edit/contacts/otp',
    loadChildren: () => import('@pages/otp/otp.module').then(m => m.OtpModule)
  }
];
