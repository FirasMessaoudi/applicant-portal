import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChangePasswordRoutingModule } from './change-password-routing.module';
import { ChangePasswordComponent } from './change-password.component';
import {ReactiveFormsModule} from "@angular/forms";
import {UserService} from "@core/services";
import {SharedModule} from "@shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        ChangePasswordRoutingModule,
        SharedModule,
        TranslateModule
    ],
  providers: [UserService],
  declarations: [ChangePasswordComponent]
})
export class ChangePasswordModule { }
