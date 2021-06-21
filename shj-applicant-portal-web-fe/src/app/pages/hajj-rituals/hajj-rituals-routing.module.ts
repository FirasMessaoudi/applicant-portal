import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HajjRitualsComponent } from './hajj-rituals.component';


const routes: Routes = [
  {path: '', component: HajjRitualsComponent}
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HajjRitualsRoutingModule { }
