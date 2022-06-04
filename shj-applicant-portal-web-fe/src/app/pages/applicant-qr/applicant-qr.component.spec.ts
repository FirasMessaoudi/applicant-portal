import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ApplicantQrComponent} from './applicant-qr.component';

describe('AboutComponent', () => {
  let component: ApplicantQrComponent;
  let fixture: ComponentFixture<ApplicantQrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicantQrComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicantQrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
