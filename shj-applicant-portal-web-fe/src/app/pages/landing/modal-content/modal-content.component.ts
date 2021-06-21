import { Component } from '@angular/core';
import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'dcc-modal-content',
  templateUrl: './modal-content.component.html'
})
export class ModalContentComponent {

  constructor(public activeModal: NgbActiveModal) { }

}
