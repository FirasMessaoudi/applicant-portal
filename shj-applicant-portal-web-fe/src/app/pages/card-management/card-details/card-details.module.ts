import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CardDetailsRoutingModule} from './card-details-routing.module';
import {MainDetailsComponent} from './main-details/main-details.component';
import {HamlahDetailsComponent} from './hamlah-details/hamlah-details.component';
import {HealthDetailsComponent} from './health-details/health-details.component';
import {TafweejDetailsComponent} from './tafweej-details/tafweej-details.component';
import {CardDetailsComponent} from './card-details.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "@shared/shared.module";
import {NgbCollapseModule, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {GroupLeadersComponent} from "@pages/card-management/card-details/group-leaders/group-leaders.component";
import {HealthProfileComponent} from './health-profile/health-profile.component';


@NgModule({
  declarations: [MainDetailsComponent, HamlahDetailsComponent, HealthDetailsComponent, TafweejDetailsComponent, CardDetailsComponent, GroupLeadersComponent, HealthProfileComponent],
    imports: [
        CommonModule,
        CardDetailsRoutingModule,
        TranslateModule,
        SharedModule,
        NgbCollapseModule,
        NgbModule
    ],

})
export class CardDetailsModule { }
