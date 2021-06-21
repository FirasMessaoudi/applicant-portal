import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {CoreModule} from '@core/core.module';
import {ServiceWorkerModule} from '@angular/service-worker';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {environment} from 'environments/environment';
import {SharedModule} from "@shared/shared.module";
import { PrintingManagementModule } from './pages/printing-management/printing-management.module';


@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    SharedModule,
    PrintingManagementModule
  ],
  exports: [],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  static injector: Injector;

  constructor(injector: Injector) {
    AppModule.injector = injector;
  }
}
