import {Routes} from "@angular/router";

export const CARD_MANAGEMENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/card-management/card-management.module').then(m => m.CardManagementModule)
  }
];
