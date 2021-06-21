import { Component, OnInit, OnDestroy, NgZone, ViewEncapsulation } from '@angular/core';
import { trigger, animate, style, transition } from '@angular/animations';
import { Router, ResolveStart, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SwUpdate } from '@angular/service-worker';
import {I18nService} from "@dcc-commons-ng/services";
import {environment} from "@env/environment";

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations: [
    trigger('fade', [
      transition(':leave', [
        animate('300ms ease-out',
          style({ opacity: 0 })
        )]
      )
    ])
  ]
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'dcc-elm-app';

  rippleInitListener: any;
  rippleMouseDownListener: any;

  public loading: boolean = true;
  private sub: Subscription;

  constructor(
    public zone: NgZone,
    private router: Router,
    private swUpdate: SwUpdate,
    private i18nService: I18nService
  ) { }

  ngOnInit() {

    this.zone.runOutsideAngular(() => { this.bindRipple(); });


    // Setup translations
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);

    // showing and hiding the loader accordingly
    this.sub = this.router.events
      .pipe(
        // Filters Resolve events only
        filter( e => e instanceof ResolveStart || e instanceof NavigationEnd ),
        // Maps the event into true/false
        map( e => e instanceof ResolveStart )
        // Shows the loader spinner while resolving the route content
      ).subscribe( e => this.loading = e );

    if (this.swUpdate.isEnabled) {

      this.swUpdate.available.subscribe(() => {

        if (confirm('New version available. Load New Version?')) {

          window.location.reload();
        }
      });
    }
  }



  bindRipple() {
    this.rippleInitListener = this.init.bind(this);
    document.addEventListener('DOMContentLoaded', this.rippleInitListener);
  }

  init() {
    this.rippleMouseDownListener = this.rippleMouseDown.bind(this);
    document.addEventListener('mousedown', this.rippleMouseDownListener, false);
  }

  rippleMouseDown(e) {
    for (let target = e.target; target && target !== this; target = target['parentNode']) {
      if (!this.isVisible(target)) {
        continue;
      }

      // Element.matches() -> https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
      if (this.selectorMatches(target, '.ripplelink, .btn, .actionIcon')) {
        const element = target;
        this.rippleEffect(element, e);
        break;
      }
    }
  }

  selectorMatches(el, selector) {
    const p = Element.prototype;
    const f = p['matches'] || p['webkitMatchesSelector'] || p['mozMatchesSelector'] || p['msMatchesSelector'] || function (s) {
      return [].indexOf.call(document.querySelectorAll(s), this) !== -1;
    };
    return f.call(el, selector);
  }

  isVisible(el) {
    return !!(el.offsetWidth || el.offsetHeight);
  }

  rippleEffect(element, e) {
    if (element.querySelector('.ink') === null) {
      const inkEl = document.createElement('span');
      this.addClass(inkEl, 'ink');

      if (this.hasClass(element, 'ripplelink') && element.querySelector('span')) {
        element.querySelector('span').insertAdjacentHTML('afterend', '<span class=\'ink\'></span>');
      } else {
        element.appendChild(inkEl);
      }
    }

    const ink = element.querySelector('.ink');
    this.removeClass(ink, 'ripple-animate');

    if (!ink.offsetHeight && !ink.offsetWidth) {
      const d = Math.max(element.offsetWidth, element.offsetHeight);
      ink.style.height = d + 'px';
      ink.style.width = d + 'px';
    }

    const x = e.pageX - this.getOffset(element).left - (ink.offsetWidth / 2);
    const y = e.pageY - this.getOffset(element).top - (ink.offsetHeight / 2);

    ink.style.top = y + 'px';
    ink.style.left = x + 'px';
    ink.style.pointerEvents = 'none';
    this.addClass(ink, 'ripple-animate');
  }
  hasClass(element, className) {
    if (element.classList) {
      return element.classList.contains(className);
    } else {
      return new RegExp('(^| )' + className + '( |$)', 'gi').test(element.className);
    }
  }

  addClass(element, className) {
    if (element.classList) {
      element.classList.add(className);
    } else {
      element.className += ' ' + className;
    }
  }

  removeClass(element, className) {
    if (element.classList) {
      element.classList.remove(className);
    } else {
      element.className = element.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
    }
  }
  getOffset(el) {
    const rect = el.getBoundingClientRect();

    return {
      top: rect.top + (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0),
      left: rect.left + (window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0),
    };
  }

  unbindRipple() {
    if (this.rippleInitListener) {
      document.removeEventListener('DOMContentLoaded', this.rippleInitListener);
    }
    if (this.rippleMouseDownListener) {
      document.removeEventListener('mousedown', this.rippleMouseDownListener);
    }
  }

  ngOnDestroy() {
    this.unbindRipple();
    this.sub.unsubscribe();
  }


}



