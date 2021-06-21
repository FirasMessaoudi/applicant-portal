import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyProfileRoutingModule } from './my-profile-routing.module';
import * as fromMyProfile from './';
import { ModalDisciplesComponent } from './step-three/modal-disciples/modal-disciples.component';
import { SharedModule } from '@app/_shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    MyProfileRoutingModule,
    SharedModule
  ],
  declarations: [
    ...fromMyProfile.my_profile,
    ModalDisciplesComponent
  ],
  entryComponents: [ModalDisciplesComponent]
})
export class MyProfileModule { }
