import { Routes } from '@angular/router';

export const QR_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/applicant-qr/applicant-qr.module').then(m => m.ApplicantQrModule)
  }
];
