import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from './register.component';
import {RegisterSuccessComponent} from "@pages/register/success/register-success.component";

const routes: Routes = [
  {path: '', component: RegisterComponent},
  {path: '/register-success', component: RegisterSuccessComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class REGISTERRoutingModule {
}
