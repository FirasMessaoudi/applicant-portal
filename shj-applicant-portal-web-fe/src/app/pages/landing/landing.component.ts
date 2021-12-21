import { Component, OnInit } from '@angular/core';
import { $animations } from '@shared/animate/animate.animations';

// importing service
import { Meta, Title } from '@angular/platform-browser';

import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { ModalContentComponent } from './modal-content/modal-content.component';

@Component({
  selector: 'dcc-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
  animations: $animations,
  host: { '[@fadeInAnimation]': '', 'class': 'd-block' }
})
export class LandingComponent implements OnInit {

  eligible = [];

  constructor(private _meta: Meta, private _title: Title, private modalService: NgbModal) {
    this._meta.updateTag({ name: 'author', content: 'Elm' });
    this._meta.updateTag({
      name: 'description',
      content:
        'describe page in this description'
    });
    this._meta.updateTag({
      name: 'keywords',
      content: 'About Elm, DCC department, Meta Service, SEO, Angular'
    });

    // adding title
    this._title.setTitle('Landing | Elm - Product Name');
  }

  ngOnInit() {
    setTimeout(() => {
        this.eligible = [
          {
            title: 'حساب المواطن',
            logo: 'assets/images/logo-ca.png',
            Description: 'استعرض المناطق المحضورة المسموحة',
            eligibility: 'true',
            eligibilityMsg: 'أنت مستحق'
          }, {
            title: 'حافز',
            logo: 'assets/images/logo-havez.png',
            Description: 'لإعانة الباحثين عن العمل',
            eligibility: 'true',
            eligibilityMsg: 'أنت مستحق'
          }, {
            title: 'بنك التنمية الاجتماعية',
            logo: 'assets/images/logo-bank.png',
            Description: 'يمنح المتأخرين في القروض فرصة السداد',
            eligibility: 'false',
            eligibilityMsg: 'الرجاء إدخال بيانات العنوان لتصبح مؤهلا'
          }, {
            title: 'صندوق تنمية الموارد البشرية',
            logo: 'assets/images/logo-hdif.png',
            Description: 'يوفر فرص التوظيف والتدريب المتاحة',
            eligibility: 'true',
            eligibilityMsg: 'أنت مستحق'
          }
        ]
      });
}



open() {
  const modalRef = this.modalService.open(ModalContentComponent, { size: 'lg' });
  modalRef.componentInstance.name = 'World';
}

}
