import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToggleDropdownDirective } from './toggle-dropdown.directive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [ToggleDropdownDirective],
  exports: [
    ToggleDropdownDirective
  ]
})
export class ToggleDropdownModule { }