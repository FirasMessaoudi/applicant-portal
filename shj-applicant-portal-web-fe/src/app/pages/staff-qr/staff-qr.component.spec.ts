import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StaffQrComponent} from './staff-qr.component';

describe('AboutComponent', () => {
  let component: StaffQrComponent;
  let fixture: ComponentFixture<StaffQrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StaffQrComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffQrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
