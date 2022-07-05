import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompanyRatingComponent } from './company-rating.component';

const routes: Routes = [
  {path: '', component: CompanyRatingComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyRatingRoutingModule { }
