import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HajjRitualsComponent } from './hajj-rituals.component';

describe('HajjRitualsComponent', () => {
  let component: HajjRitualsComponent;
  let fixture: ComponentFixture<HajjRitualsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HajjRitualsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HajjRitualsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
