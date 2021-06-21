import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintingRequestDetailsComponent } from './printing-request-details.component';

describe('PrintingRequestDetailsComponent', () => {
  let component: PrintingRequestDetailsComponent;
  let fixture: ComponentFixture<PrintingRequestDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrintingRequestDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrintingRequestDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
