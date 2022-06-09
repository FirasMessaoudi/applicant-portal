import {SharedModule} from '@app/_shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PrivacyPolicyRoutingModule} from './privacy-policy.routing.module';
import {PrivacyPolicyComponent} from './privacy-policy.component';

@NgModule({
  imports: [
    CommonModule,
    PrivacyPolicyRoutingModule,
    SharedModule
  ],
  declarations: [
    PrivacyPolicyComponent
  ]
})
export class PrivacyPolicyModule {
}
