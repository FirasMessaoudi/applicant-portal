import {SharedModule} from '@app/_shared/shared.module';
import {NgModule, Optional, SkipSelf} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import * as layout from '@core/_layout';
import {UtilityService} from '@core/utilities/utility.service';
import {EnsureModuleLoadedOnceGuard} from '@core/guards/ensure-module-loaded-once.guard';
import {RouterModule} from '@angular/router';

import {ReactiveFormsModule} from '@angular/forms';
// SVG Icons
import {SvgIconModule} from '@app/_shared/components/svg-icon/svg-icon.module';

// Toggle Dropdown
import {ToggleDropdownModule} from '@shared/modules/toggle-dropdown/toggle-dropdown.module';
import {AuthenticationService, CardService, DashboardService, RegisterService, UserService} from './services';
import {DccCommonsNgServicesModule} from "@dcc-commons-ng/services";
import {environment} from "@env/environment";
import {LOGIN_URL, PUBLIC_URL_PATTERNS} from "@core/guards/authentication.guard";
import {TranslateModule} from "@ngx-translate/core";
import {LookupService} from "@core/utilities/lookup.service";

@NgModule({
  declarations: [
    ...layout.layout,
  
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    SvgIconModule,
    TranslateModule,
    ToggleDropdownModule,
    SharedModule,
    DccCommonsNgServicesModule.forRoot(PUBLIC_URL_PATTERNS, LOGIN_URL, 'assets/i18n/'),
    TranslateModule
  ],
  exports: [HttpClientModule, ReactiveFormsModule, SvgIconModule, ToggleDropdownModule],
  providers: [
    UtilityService,
    UserService,
    DashboardService,
    RegisterService,
    AuthenticationService,
    CardService,
    LookupService,
    {provide: 'environment', useValue: environment}
  ]
})
export class CoreModule extends EnsureModuleLoadedOnceGuard {
  // Looks for the module in the parent injector to see if it's already been loaded (only want it loaded once)
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    super(parentModule);
  }
}
