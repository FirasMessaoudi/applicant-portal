import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HajjJourneyDetailsComponent } from './hajj-journey-details.component';

describe('HajjJourneyDetailsComponent', () => {
  let component: HajjJourneyDetailsComponent;
  let fixture: ComponentFixture<HajjJourneyDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HajjJourneyDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HajjJourneyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
