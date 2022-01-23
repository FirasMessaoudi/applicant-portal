import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HajjCardRoutingModule} from './hajj-card-routing.module';
import {HajjCardComponent} from './hajj-card.component';
import {BadgeComponent} from './badge/badge.component';
import {SharedModule} from '@shared/shared.module';
import {QRCodeModule} from "angularx-qrcode";


@NgModule({
  declarations: [HajjCardComponent, BadgeComponent],
  imports: [
    CommonModule,
    HajjCardRoutingModule,
    SharedModule,
    QRCodeModule

  ],
  exports:[
    BadgeComponent
  ]
})
export class HajjCardModule { }
