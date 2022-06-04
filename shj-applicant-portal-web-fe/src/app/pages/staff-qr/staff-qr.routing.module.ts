import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {StaffQrComponent} from './staff-qr.component';

const routes: Routes = [
  {path: 'staff-qr', component: StaffQrComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class StaffQrRoutingModule {
}
