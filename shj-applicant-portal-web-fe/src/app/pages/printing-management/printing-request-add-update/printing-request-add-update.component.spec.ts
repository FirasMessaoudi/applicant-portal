import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintingRequestAddUpdateComponent } from './printing-request-add-update.component';

describe('PrintingRequestAddUpdateComponent', () => {
  let component: PrintingRequestAddUpdateComponent;
  let fixture: ComponentFixture<PrintingRequestAddUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrintingRequestAddUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrintingRequestAddUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
