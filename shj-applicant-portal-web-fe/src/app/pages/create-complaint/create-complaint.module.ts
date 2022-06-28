
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CreateComplaintRoutingModule} from './create-complaint-routing.module';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {CreateComplaintComponent} from "@pages/create-complaint/create-complaint.component";
@NgModule({
  declarations: [CreateComplaintComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    CreateComplaintRoutingModule,
    TranslateModule,
  ]
})
export class CreateComplaintModule { }
