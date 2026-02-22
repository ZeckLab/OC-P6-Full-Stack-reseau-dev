import { Component, inject, signal } from '@angular/core';
import { Auth } from '../../../modules/auth/auth';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'header-mobile-user',
  imports: [RouterModule],
  templateUrl: './header-mobile-user.html',
  styleUrl: './header-mobile-user.scss',
})
export class HeaderMobileUser {
  private readonly auth = inject(Auth);

  menuOpen = signal(false);

  toggleMenu() {
    this.menuOpen.update((v) => !v);
  }

  logout() {
    this.auth.logout();
  }
}
