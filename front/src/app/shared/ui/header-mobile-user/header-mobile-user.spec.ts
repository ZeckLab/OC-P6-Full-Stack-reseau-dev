import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderMobileUser } from './header-mobile-user';

describe('HeaderMobileUser', () => {
  let component: HeaderMobileUser;
  let fixture: ComponentFixture<HeaderMobileUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderMobileUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderMobileUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
