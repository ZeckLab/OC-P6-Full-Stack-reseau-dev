import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderResolver } from './header-resolver';

describe('HeaderResolver', () => {
  let component: HeaderResolver;
  let fixture: ComponentFixture<HeaderResolver>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderResolver]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderResolver);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
