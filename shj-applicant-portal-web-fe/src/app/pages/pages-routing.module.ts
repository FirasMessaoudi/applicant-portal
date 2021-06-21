import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './';
import { CardDetailsComponent } from './card-management/card-details/card-details.component';

const routes: Routes = [
  // { path: '/', redirectTo: '/home', pathMatch: 'full' },
  // {path: '', component: HomeComponent}
  { path: '/', redirectTo: '/cards/details/1', pathMatch: 'full' },
  {path: '', component: CardDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {
}
