import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ImportantLinksRoutingModule} from './important-links-routing.module';
import {ImportantLinksComponent} from './important-links.component';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [ImportantLinksComponent],
  imports: [
    CommonModule,
    ImportantLinksRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    TranslateModule,
  ]
})
export class ImportantLinksModule { }
