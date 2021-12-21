import { Component } from '@angular/core';

import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { ModalDisciplesComponent } from './modal-disciples/modal-disciples.component';


@Component({
  selector: 'dcc-step-three',
  templateUrl: './step-three.component.html'
})
export class StepThreeComponent {

  constructor(private modalService: NgbModal) { }

  openDisciples() {
    const modalRef = this.modalService.open(ModalDisciplesComponent, { size: 'lg' });
  }

}
