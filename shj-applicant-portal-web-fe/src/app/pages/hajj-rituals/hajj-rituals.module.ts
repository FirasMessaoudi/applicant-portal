import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HajjRitualsRoutingModule } from './hajj-rituals-routing.module';
import { HajjRitualsComponent } from './hajj-rituals.component';
import { SharedModule } from '@app/_shared/shared.module';


@NgModule({
  declarations: [HajjRitualsComponent],
  imports: [
    CommonModule,
    HajjRitualsRoutingModule,
    SharedModule,
  ]
})
export class HajjRitualsModule { }
