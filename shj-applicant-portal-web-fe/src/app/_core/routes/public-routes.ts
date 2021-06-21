import { Routes } from '@angular/router';

export const PUBLIC_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/support/support.module').then(m => m.SupportModule)
  },
];
