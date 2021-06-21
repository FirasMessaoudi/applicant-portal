import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DccLayoutLandingComponent } from './dcc-layout-landing.component';

describe('DccLayoutLandingComponent', () => {
  let component: DccLayoutLandingComponent;
  let fixture: ComponentFixture<DccLayoutLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DccLayoutLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DccLayoutLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
