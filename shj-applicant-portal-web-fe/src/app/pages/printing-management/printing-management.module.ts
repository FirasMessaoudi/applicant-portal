import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PrintingManagementRoutingModule} from './printing-management-routing.module';
import {PrintingRequestListComponent} from './printing-request-list/printing-request-list.component';
import {PrintingRequestDetailsComponent} from './printing-request-details/printing-request-details.component';
import {SharedModule} from "@shared/shared.module";
import {PrintingRequestAddUpdateComponent} from './printing-request-add-update/printing-request-add-update.component';
import {PrintingRequestAddUpdateModule} from './printing-request-add-update/printing-request-add-update.module';


@NgModule({
  declarations: [PrintingRequestListComponent, PrintingRequestDetailsComponent, PrintingRequestAddUpdateComponent],
  imports: [
    CommonModule,
    PrintingManagementRoutingModule,
    SharedModule,
    PrintingRequestAddUpdateModule
  ]
})
export class PrintingManagementModule { }
