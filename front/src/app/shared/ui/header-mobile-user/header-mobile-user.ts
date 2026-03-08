import { Component, ElementRef, HostListener, inject, signal, ViewChild } from '@angular/core';
import { Auth } from '../../../modules/auth/services/auth';
import { RouterModule } from '@angular/router';

/** Mobile header for authenticated users, with a burger menu and logout action. */
@Component({
  selector: 'header-mobile-user',
  imports: [RouterModule],
  templateUrl: './header-mobile-user.html',
  styleUrl: './header-mobile-user.scss',
})
export class HeaderMobileUser {
  @ViewChild('menuRef') menuRef!: ElementRef;
  @ViewChild('burgerRef') burgerRef!: ElementRef;

  private readonly auth = inject(Auth);

  // Whether the mobile menu is open
  menuOpen = signal(false);

  // Toggles the burger menu open/closed
  toggleMenu() {
    this.menuOpen.update((v) => !v);
  }

  logout() {
    this.auth.logout();
  }

  // Closes the menu when clicking outside of it
  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (!this.menuOpen()) return;

    const menuEl = this.menuRef?.nativeElement;
    const burgerEl = this.burgerRef?.nativeElement;

    const target = event.target as Node;

    const clickedInsideMenu = menuEl?.contains(target);
    const clickedBurger = burgerEl?.contains(target);

    if (!clickedInsideMenu && !clickedBurger) {
      this.menuOpen.set(false);
    }
  }
}
