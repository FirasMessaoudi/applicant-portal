import {ChangeDetectorRef, Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-printing-request-add-update',
  templateUrl: './printing-request-add-update.component.html',
  styleUrls: ['./printing-request-add-update.component.scss']
})
export class PrintingRequestAddUpdateComponent implements OnInit {

  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
  }

  saveStepOne() {
    this.cdr.detectChanges();
  }

  saveStepTwo() {
    this.cdr.detectChanges();
  }

  confirm() {

  }

}
