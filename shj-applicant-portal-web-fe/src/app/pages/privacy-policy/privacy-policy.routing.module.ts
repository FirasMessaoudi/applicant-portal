import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PrivacyPolicyComponent} from './privacy-policy.component';

const routes: Routes = [
  {path: 'privacy-policy', component: PrivacyPolicyComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class PrivacyPolicyRoutingModule {
}