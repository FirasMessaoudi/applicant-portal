import {SharedModule} from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {StaffQrRoutingModule} from './staff-qr.routing.module';
import {StaffQrComponent} from './staff-qr.component';

@NgModule({
  imports: [
    CommonModule,
    StaffQrRoutingModule,
    SharedModule
  ],
  declarations: [
    StaffQrComponent
  ]
})
export class StaffQrModule {
}
