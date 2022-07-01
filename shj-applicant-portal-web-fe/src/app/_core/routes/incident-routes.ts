import {Routes} from "@angular/router";

export const INCIDENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/incident/incident.module').then(m => m.IncidentModule)
  }
];
