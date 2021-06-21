import { Routes } from '@angular/router';


export const USER_MANAGEMENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/user-management/user-management.module').then(m => m.UserManagementModule)
  }
];
