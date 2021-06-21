import { Component, ElementRef } from '@angular/core';
import { $animations } from '@shared/animate/animate.animations';

@Component({
  selector: 'dcc-app-layout',
  templateUrl: './app-layout.component.html',
  styleUrls: ['./app-layout.component.scss'],
  host: { 'class': 'dcc__wrapper' },
  animations: $animations
})
export class AppLayoutComponent {
  state = 'normal';
  private navClasses = {
    normal: 'wrapper-collapse',
    collapsed: 'wrapper-expand'
  };
  constructor(private el: ElementRef,) { }

  toggleNavbar() {
    let bodyClass = '';

    if (this.state === 'normal') {
      this.state = 'collapse';
      bodyClass = this.navClasses.collapsed;
    } else {
      this.state = 'normal';
      bodyClass = this.navClasses.normal;
    }

    this.el.nativeElement.closest('body').className = bodyClass;
  }

}
