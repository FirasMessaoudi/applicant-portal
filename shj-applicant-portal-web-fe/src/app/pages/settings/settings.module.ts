import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsRoutingModule} from './settings-routing.module';
import {SettingsComponent} from './settings.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "@shared/shared.module";
import {NgSelectModule} from "@ng-select/ng-select";
import {NgbDropdownModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  imports: [
    CommonModule,
    SettingsRoutingModule,
    TranslateModule,
    NgSelectModule,
    NgbDropdownModule,
    SharedModule
  ],
  declarations: [SettingsComponent]
})
export class SettingsModule {
}
