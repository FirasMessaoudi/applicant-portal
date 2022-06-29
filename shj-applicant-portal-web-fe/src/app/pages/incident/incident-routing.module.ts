import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CreateIncidentComponent} from "@pages/incident/create-incident/create-incident.component";
import {IncidentListComponent} from "@pages/incident/incident-list/incident-list.component";
import {IncidentDetailsComponent} from "@pages/incident/incident-details/incident-details.component";

const routes: Routes = [
  {path: 'incident/list', component: IncidentListComponent, canActivate: [AuthenticationGuard]},
  {path: 'incident/create', component: CreateIncidentComponent, canActivate: [AuthenticationGuard]},
  {path: 'incident/details/:id', component: IncidentDetailsComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IncidentRoutingModule { }
