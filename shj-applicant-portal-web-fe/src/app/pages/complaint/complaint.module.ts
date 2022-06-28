
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ComplaintRoutingModule} from './complaint-routing.module';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {CreateComplaintComponent} from "@pages/complaint/create-complaint/create-complaint.component";
import {ComplaintListComponent} from "@pages/complaint/complaint-list/complaint-list.component";
@NgModule({
  declarations: [CreateComplaintComponent, ComplaintListComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    ComplaintRoutingModule,
    TranslateModule,
  ]
})
export class ComplaintModule { }
