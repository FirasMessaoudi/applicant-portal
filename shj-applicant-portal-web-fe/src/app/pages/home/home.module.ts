import { SharedModule } from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

import {HomeRoutingModule} from './home.routing.module';

import {ChartsModule} from "ng2-charts";


@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    HomeRoutingModule,
    ChartsModule,
    SharedModule
  ],
  declarations: [

  ],
  providers: []
})
export class HomeModule {
}
