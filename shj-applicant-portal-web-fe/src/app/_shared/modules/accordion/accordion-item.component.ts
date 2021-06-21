import { Component, Input, Output, OnInit, HostListener, EventEmitter } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { Observable } from 'rxjs';

@Component({
  selector: 'accordion-item',
  template: `
  <div class="accordion-title" (click)="onToggle()" [ngClass]="{'active': expanded, '': !expanded}">
    <ng-content select="[title]"></ng-content>
    <img alt="" src="assets/images/svg-icons/chevron-down.svg" class="chevron"/>
  </div>
  <div class="accordion-content" [@toggle]="expanded">
    <ng-content select="[conten]"></ng-content>
  </div>
  `,
  animations: [
    trigger('toggle', [
      state('true', style({ height: '*', overflow: 'hidden' })),
      state('false', style({ height: '0px', overflow: 'hidden', opacity: '0' })),
      transition('false <=> true', animate('250ms ease-out'))
    ])
  ]
})
export class AccordionItemComponent {
  @Output() toggleEmitter: EventEmitter<any> = new EventEmitter<any>();
  expanded = true;

  onToggle() {
    this.toggleEmitter.next(this);
  }

  toggle() {
    this.expanded = !this.expanded;
  }

  close() {
    this.expanded = false;
  }
}