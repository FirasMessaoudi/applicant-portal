import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HamlahDetailsComponent } from './hamlah-details.component';

describe('HamlahDetailsComponent', () => {
  let component: HamlahDetailsComponent;
  let fixture: ComponentFixture<HamlahDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HamlahDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HamlahDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
