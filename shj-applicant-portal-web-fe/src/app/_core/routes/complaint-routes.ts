import {Routes} from "@angular/router";

export const COMPLAINT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/complaint/complaint.module').then(m => m.ComplaintModule)
  }
];
