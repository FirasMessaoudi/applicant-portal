
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CardManagementRoutingModule} from './card-management-routing.module';
import {CardListComponent} from './card-list/card-list.component';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {CardDetailsModule} from './card-details/card-details.module';
import { HajjCardModule } from './hajj-card/hajj-card.module';
@NgModule({
  declarations: [CardListComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    CardManagementRoutingModule,
    TranslateModule,
    CardDetailsModule,
    HajjCardModule
  ]
})
export class CardManagementModule { }
