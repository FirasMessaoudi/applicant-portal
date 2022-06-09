import { Routes } from '@angular/router';

export const PUBLIC_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/support/support.module').then(m => m.SupportModule)
  },
  {
    path: '',
    loadChildren: () => import('@pages/staff-qr/staff-qr.module').then(m => m.StaffQrModule)
  },
  {
    path: '',
    loadChildren: () => import('@pages/applicant-qr/applicant-qr.module').then(m => m.ApplicantQrModule)
  },
  {
    path: '',
    loadChildren: () => import('@pages/privacy-policy/privacy-policy.module').then(m => m.PrivacyPolicyModule)
  },
];
