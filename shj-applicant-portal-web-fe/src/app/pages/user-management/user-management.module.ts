import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {UserManagementRoutingModule} from './user-management.routing.module';
import {UserListComponent} from './user-list/user-list.component';
import {UserDetailsComponent} from './user-details/user-details.component';
import {SharedModule} from '@app/_shared/shared.module';
import {UserService} from '@app/_core/services';
import {TranslateModule} from "@ngx-translate/core";
import {UserAddUpdateComponent} from './user-add-update/user-add-update.component';
import {HijriGregorianDatepickerModule} from "@shared/modules/hijri-gregorian-datepicker/hijri-gregorian-datepicker.module";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        NgbModule,
        UserManagementRoutingModule,
        SharedModule,
        TranslateModule,
        HijriGregorianDatepickerModule
    ],

  declarations: [
    UserListComponent,
    UserDetailsComponent,
    UserAddUpdateComponent
  ],
  entryComponents: [],
  providers: [
    UserService, NgbActiveModal
  ]
})
export class UserManagementModule {
}
