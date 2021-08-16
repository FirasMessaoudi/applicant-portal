import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsRoutingModule } from './settings-routing.module';
import { SettingsComponent } from './settings.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "@shared/shared.module";

@NgModule({
  imports: [
    CommonModule,
    SettingsRoutingModule,
    TranslateModule,
    SharedModule
  ],
  declarations: [SettingsComponent]
})
export class SettingsModule { }
