import { Component } from '@angular/core';
import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'dcc-modal-disciples',
  templateUrl: './modal-disciples.component.html',
  styleUrls: ['./modal-disciples.component.scss']
})
export class ModalDisciplesComponent {

  constructor(public activeModal: NgbActiveModal) { }

}
