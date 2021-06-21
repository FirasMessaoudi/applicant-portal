import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegisterComponent} from './register.component';
import {REGISTERRoutingModule} from './register-routing.module';

import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {SharedModule} from "@shared/shared.module";
import {HijriGregorianDatepickerModule} from "@shared/modules/hijri-gregorian-datepicker/hijri-gregorian-datepicker.module";

@NgModule({
  imports: [
    CommonModule,
    REGISTERRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    SharedModule,
    HijriGregorianDatepickerModule
  ],
  declarations: [
    RegisterComponent
  ]
})
export class RegisterModule {

}
