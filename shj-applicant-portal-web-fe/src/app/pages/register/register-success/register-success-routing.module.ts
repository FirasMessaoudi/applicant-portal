import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterSuccessComponent} from "@pages/register/register-success/register-success.component";

const routes: Routes = [
  {path: '', component: RegisterSuccessComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegisterSuccessRoutingModule {
}
