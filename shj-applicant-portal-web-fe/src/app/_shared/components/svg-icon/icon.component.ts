import { Component, HostBinding, Input } from '@angular/core';

@Component({
  selector: 'svg-icon',
  template: `
  <i [class]='styleClass'
  [inlineSVG]="${'pathSVG'} + ${'icon'} + ${'extSVG'}">
  </i>
  `,
  host: { class: 'dcc-svg' },
})
export class IconComponent {

  @HostBinding('attr.styleClass')
  @Input() styleClass: string;
  @Input() icon: string;

  // @HostBinding('attr.prefix')
  // @Input() prefix: 'brands'|'duotone'|'light'|'solid'|'regular' = 'regular';

  public pathSVG = 'assets/images/svg-icons/';
  public extSVG = '.svg';

}
