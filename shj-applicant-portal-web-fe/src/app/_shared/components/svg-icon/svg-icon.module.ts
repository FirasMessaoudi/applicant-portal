import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// SVG Icons
import { InlineSVGModule } from 'ng-inline-svg';
import { IconComponent } from './icon.component';

@NgModule({
  imports: [
    CommonModule,
    InlineSVGModule.forRoot(),
  ],
  declarations: [IconComponent],
  exports: [
    IconComponent
  ]
})
export class SvgIconModule { }
