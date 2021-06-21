import { Directive, HostBinding, ElementRef, OnDestroy, Renderer2 } from '@angular/core';

@Directive({
  selector: '.dropdown'
})
export class ToggleDropdownDirective {

  @HostBinding('class.show')
  public isOpen: boolean;

  private buttonMousedown: () => void;
  private buttonBlur: () => void;
  private navMousedown: () => void;
  private navClick: () => void;

  constructor(private element: ElementRef, private renderer: Renderer2) { }

  ngAfterViewInit() {
    const el = this.element.nativeElement;
    const btnElem = el.querySelector('.dropdown-toggle');
    const menuElem = el.querySelector('.dropdown-menu');

    this.buttonMousedown = this.renderer.listen(btnElem, 'mousedown', (evt) => {
      console.log('MOUSEDOWN BTN');
      this.isOpen = !this.isOpen;
      evt.preventDefault(); // prevents loose of focus (default behaviour) on some browsers
    });

    this.buttonMousedown = this.renderer.listen(btnElem, 'click', () => {
      console.log('CLICK BTN');
      // firefox OSx, Safari, Ie OSx, Mobile browsers.
      // Whether clicking on a <button> causes it to become focused varies by browser and OS.
      btnElem.focus();
    });

    // only for debug
    this.buttonMousedown = this.renderer.listen(btnElem, 'focus', () => {
      console.log('FOCUS BTN');
    });

    this.buttonBlur = this.renderer.listen(btnElem, 'blur', () => {
      console.log('BLUR BTN');
      this.isOpen = false;
    });

    this.navMousedown = this.renderer.listen(menuElem, 'mousedown', (evt) => {
      console.log('MOUSEDOWN MENU');
      evt.preventDefault(); // prevents nav element to get focus and button blur event to fire too early
    });
    this.navClick = this.renderer.listen(menuElem, 'click', () => {
      console.log('CLICK MENU');
      this.isOpen = false;
      btnElem.blur();
    });
  }

  ngOnDestroy() {
    this.buttonMousedown();
    this.buttonBlur();
    this.navMousedown();
    this.navClick();
  }
}