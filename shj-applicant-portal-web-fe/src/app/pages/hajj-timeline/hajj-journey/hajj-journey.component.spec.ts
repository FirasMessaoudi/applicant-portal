import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HajjJourneyComponent } from './hajj-journey.component';

describe('HajjJourneyComponent', () => {
  let component: HajjJourneyComponent;
  let fixture: ComponentFixture<HajjJourneyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HajjJourneyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HajjJourneyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
