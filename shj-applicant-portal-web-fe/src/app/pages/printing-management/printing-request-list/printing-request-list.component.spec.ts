import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintingRequestListComponent } from './printing-request-list.component';

describe('PrintingRequestListComponent', () => {
  let component: PrintingRequestListComponent;
  let fixture: ComponentFixture<PrintingRequestListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrintingRequestListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrintingRequestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
