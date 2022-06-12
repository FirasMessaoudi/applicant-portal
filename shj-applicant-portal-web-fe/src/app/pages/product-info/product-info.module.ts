import {SharedModule} from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ProductInfoRoutingModule} from './product-info.routing.module';
import {ProductInfoComponent} from './product-info.component';

@NgModule({
  imports: [
    CommonModule,
    ProductInfoRoutingModule,
    SharedModule
  ],
  declarations: [
    ProductInfoComponent
  ]
})
export class ProductInfoModule {
}
