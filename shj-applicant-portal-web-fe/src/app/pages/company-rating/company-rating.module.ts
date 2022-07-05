import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompanyRatingComponent } from './company-rating.component';
import {SharedModule} from "@shared/shared.module";
import { CompanyRatingRoutingModule } from './company-rating-routing.module';

@NgModule({
  imports: [
    CommonModule,
    CompanyRatingRoutingModule,
    SharedModule
  ],
  declarations: [CompanyRatingComponent]
})
export class CompanyRatingModule { }
