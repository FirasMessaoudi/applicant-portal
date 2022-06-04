import {SharedModule} from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ApplicantQrRoutingModule} from './applicant-qr.routing.module';
import {ApplicantQrComponent} from './applicant-qr.component';

@NgModule({
  imports: [
    CommonModule,
    ApplicantQrRoutingModule,
    SharedModule
  ],
  declarations: [
    ApplicantQrComponent
  ]
})
export class ApplicantQrModule {
}
