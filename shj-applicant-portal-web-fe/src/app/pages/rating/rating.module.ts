import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RatingComponent } from './rating.component';
import {SharedModule} from "@shared/shared.module";
import { RatingRoutingModule } from './rating-routing.module';

@NgModule({
  imports: [
    CommonModule,
    RatingRoutingModule,
    SharedModule
  ],
  declarations: [RatingComponent]
})
export class RatingModule { }
