import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {CoreModule} from '@core/core.module';
import {ServiceWorkerModule} from '@angular/service-worker';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {environment} from 'environments/environment';
import {SharedModule} from "@shared/shared.module";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    SharedModule,
    BrowserAnimationsModule
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
