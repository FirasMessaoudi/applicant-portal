import {Routes} from "@angular/router";

export const COMPLAINT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/create-complaint/create-complaint.module').then(m => m.CreateComplaintModule)
  }
];
