import { Routes } from '@angular/router';

export const LANDING_ROUTES: Routes = [
  {
    path: 'landing',
    loadChildren: () => import('@pages/main-landing/main-landing.module').then(m => m.MainLandingModule)
  }
];
