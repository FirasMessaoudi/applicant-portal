/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DccLayoutQrComponent } from './dcc-layout-qr.component';

describe('DccLayoutQrComponent', () => {
  let component: DccLayoutQrComponent;
  let fixture: ComponentFixture<DccLayoutQrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DccLayoutQrComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DccLayoutQrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
