import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderDesktop } from './header-desktop';

describe('HeaderDesktop', () => {
  let component: HeaderDesktop;
  let fixture: ComponentFixture<HeaderDesktop>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderDesktop]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderDesktop);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
