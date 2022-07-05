import {Component, EventEmitter, Injectable, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {NgbModal, NgbModalOptions, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ModalDialogConfig} from '@model/modal-dialog-models/modal-dialog-config';
import {FormGroup} from '@angular/forms';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'app-modal-dialog',
  templateUrl: './modal-dialog.component.html',
  styleUrls: ['./modal-dialog.component.scss']
})

@Injectable()
export class ModalDialogComponent implements OnInit {
  @Input() public modalConfig: ModalDialogConfig;
  @Input() public formGroup?: FormGroup;
  @Output() submitForm?: EventEmitter<any> = new EventEmitter();
  @Output() dismissModal?: EventEmitter<any> = new EventEmitter();
  @ViewChild('modal') private modalDialogContent: TemplateRef<ModalDialogComponent>;
  private modalDialogRef: NgbModalRef;

  constructor(
    private modalService: NgbModal
  ) {
  }

  ngOnInit(): void {

  }

  open(modalOptions?: NgbModalOptions): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      this.modalDialogRef = this.modalService.open(this.modalDialogContent, modalOptions);
      this.modalDialogRef.result.then(resolve, resolve);
    });
  }

  async confirm(): Promise<void> {
    if (this.formGroup) {
      this.formGroup.markAllAsTouched();
      if (this.formGroup.invalid) {
        return;
      }
    }
    if (this.submitForm) {
      this.submitForm.emit();
    }
    if (this.modalConfig.shouldClose === undefined || (await this.modalConfig.shouldClose())) {
      const result = this.modalConfig.onClose === undefined || (await this.modalConfig.onClose());
      this.modalDialogRef.close(result);
    }
  }

  async dismiss(): Promise<void> {
    if (this.formGroup) {
      this.formGroup.reset();
    }
    if (this.dismissModal){
      this.dismissModal.emit();
    }
    if (this.modalConfig.shouldDismiss === undefined || (await this.modalConfig.shouldDismiss())) {
      const result = this.modalConfig.onDismiss === undefined || (await this.modalConfig.onDismiss());
      this.modalDialogRef.dismiss(result);
    }
  }

}
