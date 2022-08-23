import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ImportantLinksComponent} from "@pages/important-links/important-links.component";


const routes: Routes = [
  {path: '', component: ImportantLinksComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ImportantLinksRoutingModule { }
