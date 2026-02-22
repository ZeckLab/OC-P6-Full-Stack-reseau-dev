import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderMobileGuest } from './header-mobile-guest';

describe('HeaderMobileGuest', () => {
  let component: HeaderMobileGuest;
  let fixture: ComponentFixture<HeaderMobileGuest>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderMobileGuest]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderMobileGuest);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
