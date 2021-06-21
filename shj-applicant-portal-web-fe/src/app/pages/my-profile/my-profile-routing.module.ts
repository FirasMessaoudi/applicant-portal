import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {MyProfileComponent} from './';

const routes: Routes = [
  {path: 'profile', component: MyProfileComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MyProfileRoutingModule {
}
