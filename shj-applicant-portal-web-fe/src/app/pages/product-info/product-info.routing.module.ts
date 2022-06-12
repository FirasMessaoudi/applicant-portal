import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProductInfoComponent} from './product-info.component';

const routes: Routes = [
  {path: 'product-info', component: ProductInfoComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class ProductInfoRoutingModule {
}
