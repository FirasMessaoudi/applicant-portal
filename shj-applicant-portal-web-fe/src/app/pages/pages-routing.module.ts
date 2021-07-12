import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CardDetailsComponent} from './card-management/card-details/card-details.component';

const routes: Routes = [
  {path: '/', redirectTo: '/cards/details/1', pathMatch: 'full' },
  {path: '', component: CardDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {
}
