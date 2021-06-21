import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import { HajjTimelineRoutingModule } from './hajj-timeline-routing.module';
import { HajjJourneyDetailsComponent } from './hajj-journey-details/hajj-journey-details.component';
import { HajjJourneyComponent } from './hajj-journey/hajj-journey.component';


@NgModule({
  declarations: [HajjJourneyComponent,HajjJourneyDetailsComponent],
  imports: [
    CommonModule,
    SharedModule,
    TranslateModule,
    HajjTimelineRoutingModule
  ]
})
export class HajjTimelineModule { }
