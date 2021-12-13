import {Injectable, Input} from '@angular/core';

import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ConfirmDialogComponent} from './confirm-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDialogService {

  constructor(private modalService: NgbModal) {
  }

  @Input() title: string = 'general.dialog_confirmation_title';
  @Input() message: string = 'general.dialog_confirmation_text';
  @Input() btnOkText: string = 'general.dialog_confirmation_ok';
  @Input() btnCancelText: string = 'general.dialog_confirmation_cancel';

  public confirm(
    message: string = 'general.dialog_confirmation_text',
    title: string = 'general.dialog_confirmation_title',
    btnOkText: string = 'general.dialog_confirmation_ok',
    btnCancelText: string = 'general.dialog_confirmation_cancel',
    dialogSize: 'sm' | 'lg' = 'sm'): Promise<boolean> {
    const modalRef = this.modalService.open(ConfirmDialogComponent, {size: dialogSize, centered: true});
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnOkText = btnOkText;
    modalRef.componentInstance.btnCancelText = btnCancelText;

    return modalRef.result;
  }

}
