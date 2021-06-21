import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PagesRoutingModule} from './pages-routing.module';

import * as fromPages from './';
import {ModalContentComponent} from './landing/modal-content/modal-content.component';
import {SharedModule} from '@app/_shared/shared.module';
import {TranslateModule} from "@ngx-translate/core";
import {ChartsModule} from "ng2-charts";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";


@NgModule({
  declarations: [
    ...fromPages.pages,
    ModalContentComponent
    
  ],
    imports: [
        CommonModule,
        SharedModule,
        PagesRoutingModule,
        TranslateModule,
        ChartsModule,
        NgbModule
    ],
  entryComponents: [ModalContentComponent]
})
export class PagesModule {
}
