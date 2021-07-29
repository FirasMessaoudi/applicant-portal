import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {RegisterSuccessRoutingModule} from './register-success-routing.module';
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {RegisterSuccessComponent} from "@pages/register/register-success/register-success.component";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RegisterSuccessRoutingModule,
    SharedModule,
    TranslateModule
  ],
  providers: [],
  declarations: [RegisterSuccessComponent]
})
export class RegisterSuccessModule {
}
