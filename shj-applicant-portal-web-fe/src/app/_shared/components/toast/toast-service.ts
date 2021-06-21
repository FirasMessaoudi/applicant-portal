import {Injectable, TemplateRef} from '@angular/core';

@Injectable({providedIn: 'root'})
export class ToastService {
  toasts: any[] = [];

  show(textOrTpl: string | TemplateRef<any>, options: any = {}) {
    this.toasts.push({textOrTpl, ...options});
  }

  remove(toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }

  success(message: string, title: string) {
    this.show(message, {classname: 'bg-success text-light', delay: 5000, autohide: true, headertext: title});
  }

  error(message: string, title: string) {
    this.show(message, {classname: 'bg-danger text-light', delay: 5000, autohide: true, headertext: title});
  }

  info(message: string, title: string) {
    this.show(message, {classname: 'bg-info text-light', delay: 5000, autohide: true, headertext: title});
  }

  warning(message: string, title: string) {
    this.show(message, {classname: 'bg-warning text-light', delay: 5000, autohide: true, headertext: title});
  }

}
