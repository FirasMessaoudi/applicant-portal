import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RoleManagementRoutingModule} from './role-management.routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RoleListComponent} from './role-list/role-list.component';
import {SharedModule} from '@app/_shared/shared.module';
import {TextMaskModule} from "angular2-text-mask";
import {TranslateModule} from "@ngx-translate/core";
import {RoleDetailsComponent} from "@pages/role-management/role-details/role-details.component";
import {RoleAddUpdateComponent} from "@pages/role-management/role-add-update/role-add-update.component";

@NgModule({
  declarations: [
    RoleListComponent,
    RoleDetailsComponent,
    RoleAddUpdateComponent
  ],
  imports: [
    CommonModule,
    RoleManagementRoutingModule,
    SharedModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    TextMaskModule,
    TranslateModule,

  ],
  providers: []
})
export class RoleManagementModule {
}
