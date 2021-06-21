import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import { HajjJourneyDetailsComponent } from './hajj-journey-details/hajj-journey-details.component';
import { HajjJourneyComponent } from './hajj-journey/hajj-journey.component';


const routes: Routes = [
  {path: 'list', component: HajjJourneyComponent, canActivate: [AuthenticationGuard]},
  {path: 'details', component: HajjJourneyDetailsComponent, canActivate: [AuthenticationGuard]}
 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HajjTimelineRoutingModule { }
