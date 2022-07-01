
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {IncidentRoutingModule} from './incident-routing.module';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {CreateIncidentComponent} from "@pages/incident/create-incident/create-incident.component";
import {IncidentListComponent} from "@pages/incident/incident-list/incident-list.component";
import {IncidentDetailsComponent} from "@pages/incident/incident-details/incident-details.component";
@NgModule({
  declarations: [CreateIncidentComponent, IncidentListComponent, IncidentDetailsComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    IncidentRoutingModule,
    TranslateModule,
  ]
})
export class IncidentModule { }
