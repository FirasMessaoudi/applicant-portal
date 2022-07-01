import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CreateComplaintComponent} from "@pages/complaint/create-complaint/create-complaint.component";
import {ComplaintListComponent} from "@pages/complaint/complaint-list/complaint-list.component";
import {ComplaintDetailsComponent} from "@pages/complaint/complaint-details/complaint-details.component";

const routes: Routes = [
  {path: 'complaint/list', component: ComplaintListComponent, canActivate: [AuthenticationGuard]},
  {path: 'complaint/create', component: CreateComplaintComponent, canActivate: [AuthenticationGuard]},
  {path: 'complaint/details/:id', component: ComplaintDetailsComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ComplaintRoutingModule { }
