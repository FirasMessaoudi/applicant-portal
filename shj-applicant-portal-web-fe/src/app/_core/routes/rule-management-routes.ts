import { Routes } from '@angular/router';


export const RULE_MANAGEMENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/rule-management/rule-management.module').then(m => m.RuleManagementModule)
  }
];
