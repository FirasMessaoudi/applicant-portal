import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ApplicantQrComponent} from './applicant-qr.component';

const routes: Routes = [
  {path: 'pilgrim-qr', component: ApplicantQrComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class ApplicantQrRoutingModule {
}
