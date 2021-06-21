import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

import {ResetPasswordRoutingModule} from './reset-password.routing.module';
import {ResetPasswordComponent} from './reset-password.component';
import {SharedModule} from "@shared/shared.module";
import {HijriGregorianDatepickerModule} from "@shared/modules/hijri-gregorian-datepicker/hijri-gregorian-datepicker.module";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ResetPasswordRoutingModule,
    SharedModule,
    HijriGregorianDatepickerModule
  ],
  declarations: [
    ResetPasswordComponent
  ]
})
export class ResetPasswordModule {
}
