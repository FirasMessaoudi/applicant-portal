import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './bi-login.component';
import {LoginRoutingModule} from './bi-login-routing.module';

import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {SharedModule} from '@app/_shared/shared.module';


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LoginRoutingModule,
    HttpClientModule,
    SharedModule
  ],
  declarations: [
    LoginComponent
  ]
})
export class LoginModule {

}