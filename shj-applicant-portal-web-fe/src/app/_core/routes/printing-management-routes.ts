import {Routes} from "@angular/router";

export const PRINTING_MANAGEMENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/printing-management/printing-management.module').then(m => m.PrintingManagementModule)
  }
];
