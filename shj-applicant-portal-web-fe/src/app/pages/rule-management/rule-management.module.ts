import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {RuleManagementRoutingModule} from './rule-management.routing.module';
import {SharedModule} from '@app/_shared/shared.module';
import {UserService} from '@app/_core/services';
import {TranslateModule} from "@ngx-translate/core";
import {HijriGregorianDatepickerModule} from "@shared/modules/hijri-gregorian-datepicker/hijri-gregorian-datepicker.module";
import {RuleListComponent} from "@pages/rule-management/rule-list/rule-list.component";
import {RuleEditorComponent} from "@pages/rule-management/rule-editor/rule-editor.component";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        NgbModule,
        RuleManagementRoutingModule,
        SharedModule,
        TranslateModule,
        HijriGregorianDatepickerModule
    ],

  declarations: [
    RuleListComponent,
    RuleEditorComponent
  ],
  entryComponents: [],
  providers: [
    UserService, NgbActiveModal
  ]
})
export class RuleManagementModule {
}
