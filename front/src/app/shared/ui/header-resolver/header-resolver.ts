import { Component, inject, input, signal, WritableSignal } from '@angular/core';
import { HeaderDesktop } from '../header-desktop/header-desktop';
import { HeaderMobileGuest } from '../header-mobile-guest/header-mobile-guest';
import { HeaderMobileUser } from '../header-mobile-user/header-mobile-user';
import { Auth } from '../../../modules/auth/services/auth';

/** Chooses which header to display (desktop, mobile guest, mobile user) based on screen size and authentication. */
@Component({
  selector: 'app-header-resolver',
  standalone: true,
  templateUrl: './header-resolver.html',
  styleUrl: './header-resolver.scss',
  imports: [HeaderDesktop, HeaderMobileGuest, HeaderMobileUser]
})
export class HeaderResolver {
  private readonly auth = inject(Auth);
  
  showHeader = input<boolean>();

  isMobile = signal(window.innerWidth <= 600);

  isAuthenticated = this.auth.isAuthenticated;

  constructor() {
    window.addEventListener('resize', () => {
      this.isMobile.set(window.innerWidth <= 600);
    });
  }
}
