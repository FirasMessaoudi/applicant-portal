import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CreateComplaintComponent} from "@pages/create-complaint/create-complaint.component";

const routes: Routes = [
  {path: 'complaint/create', component: CreateComplaintComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreateComplaintRoutingModule { }
