export class RoleManagementRoutes {
}
import { Routes } from '@angular/router';

export const ROLE_MANAGEMENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/role-management/role-management.module').then(m => m.RoleManagementModule)
  }
];
