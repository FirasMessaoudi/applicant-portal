import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TafweejDetailsComponent } from './tafweej-details.component';

describe('TafweejDetailsComponent', () => {
  let component: TafweejDetailsComponent;
  let fixture: ComponentFixture<TafweejDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TafweejDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TafweejDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
