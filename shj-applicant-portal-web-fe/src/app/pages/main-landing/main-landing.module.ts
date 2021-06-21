import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainLandingRoutingModule } from './main-landing-routing.module';
import { MainLandingComponent } from './main-landing.component';


@NgModule({
  declarations: [MainLandingComponent],
  imports: [
    CommonModule,
    MainLandingRoutingModule
  ]
})
export class MainLandingModule { }
