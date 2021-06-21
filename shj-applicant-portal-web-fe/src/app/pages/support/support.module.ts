import { SharedModule } from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

import {SupportRoutingModule} from './support.routing.module';
import {SupportComponent} from './support.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import {CoreModule} from "@core/core.module";
import {ReactiveFormsModule} from "@angular/forms";
import {LoginRoutingModule} from "@pages/login/login-routing.module";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  imports: [
    CommonModule,
    SupportRoutingModule,
    SharedModule
  ],
  declarations: [
    SupportComponent
  ]
})
export class SupportModule {
}
