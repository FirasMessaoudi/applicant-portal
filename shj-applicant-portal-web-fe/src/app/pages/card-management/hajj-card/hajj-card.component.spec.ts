import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HajjCardComponent } from './hajj-card.component';

describe('HajjCardComponent', () => {
  let component: HajjCardComponent;
  let fixture: ComponentFixture<HajjCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HajjCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HajjCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
