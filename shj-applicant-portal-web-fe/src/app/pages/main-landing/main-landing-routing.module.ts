import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainLandingComponent} from "./main-landing.component";



const routes: Routes = [
  {path: '', component: MainLandingComponent}
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainLandingRoutingModule { }
