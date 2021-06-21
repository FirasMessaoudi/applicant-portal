import {Component, OnInit, Inject, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {

  @Input() title: string = 'general.dialog_confirmation_title';
  @Input() message: string = 'general.dialog_confirmation_text';
  @Input() btnOkText: string = 'general.dialog_confirmation_ok';
  @Input() btnCancelText: string = 'general.dialog_confirmation_cancel';

  constructor(private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  public decline() {
    this.activeModal.close(false);
  }

  public accept() {
    this.activeModal.close(true);
  }

  public dismiss() {
    this.activeModal.close(false);
  }

}
