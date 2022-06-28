import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CreateIncidentComponent} from "@pages/incident/create-incident/create-incident.component";
import {IncidentListComponent} from "@pages/incident/incident-list/incident-list.component";

const routes: Routes = [
  {path: 'incident/list', component: IncidentListComponent, canActivate: [AuthenticationGuard]},
  {path: 'incident/create', component: CreateIncidentComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IncidentRoutingModule { }
