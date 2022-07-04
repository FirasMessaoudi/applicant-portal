import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsRoutingModule} from './settings-routing.module';
import {SettingsComponent} from './settings.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "@shared/shared.module";
import {NgbDropdownModule} from "@ng-bootstrap/ng-bootstrap";
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SettingsRoutingModule,
    TranslateModule,
    NgbDropdownModule,
    SharedModule,
    ReactiveFormsModule
  ],
  declarations: [SettingsComponent]
})
export class SettingsModule {
}
